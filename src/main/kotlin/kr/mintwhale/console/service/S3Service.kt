package kr.mintwhale.console.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.exif.ExifIFD0Directory
import kr.mintwhale.console.data.model.FileInfo
import kr.mintwhale.console.mapper.dao.StoreFileMapper
import kr.mintwhale.console.util.Etc
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.*
import java.util.*
import javax.imageio.ImageIO


@Service
class S3Service {
    var log = LoggerFactory.getLogger(this::class.java) as Logger

    @Autowired
    private lateinit var amazonS3: AmazonS3

    @Value("\${cloud.aws.s3.bucket}")
    private lateinit var bucketName: String

    val SET_SIZE = 1024

    @Throws(IOException::class)
    fun rotateImageForMobile(file: File?): BufferedImage? {
        var orientation = 1

        try {
            val metadata = ImageMetadataReader.readMetadata(file)
            val directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory::class.java)
            if (directory != null) {
                orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION) // 회전정보
            }
        } catch (e: Exception) {

        }

        val bi = ImageIO.read(file)
        return if (orientation == 6) { //정위치
            rotateImage(bi, 90)
        } else if (orientation == 1) { //왼쪽으로 눞였을때
            bi
        } else if (orientation == 3) { //오른쪽으로 눞였을때
            rotateImage(bi, 180)
        } else if (orientation == 8) { //180도
            rotateImage(bi, 270)
        } else {
            bi
        }
    }

    fun rotateImage(orgImage: BufferedImage, radians: Int): BufferedImage? {
        val newImage: BufferedImage = if (radians == 90 || radians == 270) {
            BufferedImage(orgImage.height, orgImage.width, orgImage.type)
        } else if (radians == 180) {
            BufferedImage(orgImage.width, orgImage.height, orgImage.type)
        } else {
            return orgImage
        }

        val graphics = newImage.graphics as Graphics2D
        graphics.rotate(
            Math.toRadians(radians.toDouble()),
            (newImage.width / 2).toDouble(),
            (newImage.height / 2).toDouble()
        )
        graphics.translate((newImage.width - orgImage.width) / 2, (newImage.height - orgImage.height) / 2)
        graphics.drawImage(orgImage, 0, 0, orgImage.width, orgImage.height, null)
        return newImage
    }

    @Throws(IOException::class)
    fun upload(file: MultipartFile, dir: String): FileInfo {
        val array = file.originalFilename.toString().split(".")
        var ext = array[array.size - 1].toString().lowercase()
        if(ext == "jpeg") {
            ext = "jpg"
        }

        val objMeta = ObjectMetadata()
        val os = ByteArrayOutputStream()
        var byteArrayIs : InputStream? = null
        var oFile: File? = null

        val path = File("/tmp/oldfile/")

        log.debug(path.absolutePath)

        if(!path.exists()) {
            path.mkdirs()
        }

        if(ext == "png" || ext == "jpg") {
            oFile = File(path.absolutePath + "/" + file.originalFilename)
            file.transferTo(oFile)

            var bfimg = rotateImageForMobile(oFile)

            if (bfimg != null && (ext == "png" || bfimg.width > SET_SIZE || bfimg.height > SET_SIZE)) {
                var iwidth = bfimg.width
                var iheight = bfimg.height

                if(bfimg.width > bfimg.height && bfimg.width > SET_SIZE) {
                    iwidth = SET_SIZE
                    iheight = ((bfimg.height.toDouble() / bfimg.width.toDouble()) * SET_SIZE).toInt()
                } else if (bfimg.width < bfimg.height && bfimg.height > SET_SIZE) {
                    iwidth = ((bfimg.width.toDouble() / bfimg.height.toDouble()) * SET_SIZE).toInt()
                    iheight = SET_SIZE
                }

                val pFile = File(path.absolutePath + file.originalFilename.toString().replace(".png", ".jpg"))
                val result = BufferedImage(
                    iwidth,
                    iheight,
                    BufferedImage.TYPE_INT_RGB
                )

                if(bfimg.width != iwidth) {
                    result.createGraphics().drawImage(bfimg.getScaledInstance(iwidth, iheight, Image.SCALE_DEFAULT), 0, 0, null)
                } else {
                    result.createGraphics().drawImage(bfimg, 0, 0, null)
                }

                ImageIO.write(result, "png", pFile)
                bfimg = ImageIO.read(pFile)
                oFile.delete()
                ext = "png"
                oFile = pFile
            }

            ImageIO.write(bfimg, "png", os)

            objMeta.contentLength = os.toByteArray().size.toLong()
            byteArrayIs = ByteArrayInputStream(os.toByteArray())
        } else {
            objMeta.contentLength = file.bytes.size.toLong()
            byteArrayIs = file.inputStream
        }

        val fileName = UUID.randomUUID().toString() + "-" + Etc.randomRange(1111, 9999).toString() + "." + ext

        amazonS3.putObject(
            PutObjectRequest(bucketName, dir + fileName, byteArrayIs, objMeta)
                .withCannedAcl(CannedAccessControlList.PublicRead)
        )

        oFile?.delete()

        val finfo = FileInfo()
        finfo.name = fileName
        finfo.url = amazonS3.getUrl(bucketName, dir + fileName).toString()
        finfo.ext = ext
        finfo.size = amazonS3.getObjectMetadata(bucketName, dir + fileName).getContentLength()

        return finfo
    }

    @Throws(IOException::class)
    fun delete(url: String) {
        var seturl = url
        if(url.indexOf("http") > -1) {
            seturl = ""
            val array = url.split("/")
            for (i in 3.. (array.size -1)) {
                if(i > 3) {
                    seturl += "/" + array[i]
                } else {
                    seturl += array[i]
                }
            }
        }
        if (amazonS3.doesObjectExist(bucketName, seturl)) {
            amazonS3.deleteObject(bucketName, seturl)
        }
    }
}
