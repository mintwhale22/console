package kr.mintwhale.console.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.exif.ExifIFD0Directory
import kr.mintwhale.console.util.Etc
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.*
import java.util.*
import javax.imageio.ImageIO


@Service
class S3Service {

    var log = LoggerFactory.getLogger(this::class.java) as Logger

    @Autowired
    lateinit var s3Client: AmazonS3

    @Value("\${do.space.bucket}")
    private val doSpaceBucket: String? = null

    @Value("\${do.space.endpoint}")
    private val doSpaceEndpoint: String? = null

    @Value("\${do.space.region}")
    private val doSpaceRegion: String? = null

    @Value("\${do.space.changedns}")
    private val doChangeDns: String? = null

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
    fun upload(file: MultipartFile, dir: String): String {

        val array = file.originalFilename.toString().split(".")
        var ext = array[array.size - 1].toString().lowercase()
        if(ext == "jpeg") {
            ext = "jpg"
        }

        val objMeta = ObjectMetadata()
        val os = ByteArrayOutputStream()
        var byteArrayIs : InputStream? = null
        var oFile: File? = null

        val path = File("./oldfile/")

        if(!path.exists()) {
            path.mkdir()
        }

        if(ext == "png" && ext == "jpg") {
            oFile = File(path.absolutePath + file.originalFilename)
            file.transferTo(oFile)

            var bfimg = rotateImageForMobile(oFile)
            if (bfimg != null && ext == "png") {
                val pFile = File(path.absolutePath + file.originalFilename.toString().replace(".png", ".jpg"))
                val result = BufferedImage(
                    bfimg.width,
                    bfimg.height,
                    BufferedImage.TYPE_INT_RGB
                )
                result.createGraphics().drawImage(bfimg, 0, 0, Color.WHITE, null)
                ImageIO.write(result, "jpg", pFile)
                bfimg = ImageIO.read(pFile)
                oFile.delete()
                ext = "jpg"
                oFile = pFile
            }

            ImageIO.write(bfimg, "jpg", os)

            objMeta.contentLength = os.toByteArray().size.toLong()
            byteArrayIs = ByteArrayInputStream(os.toByteArray())
        } else {
            objMeta.contentLength = file.bytes.size.toLong()
            byteArrayIs = file.inputStream
        }

        val fileName = UUID.randomUUID().toString() + "-" + Etc.randomRange(1111, 9999).toString() + "." + ext

        s3Client.putObject(
            PutObjectRequest(doSpaceBucket, dir + fileName, byteArrayIs, objMeta)
                .withCannedAcl(CannedAccessControlList.PublicRead)
        )

        oFile?.delete()

        return s3Client.getUrl(doSpaceBucket, dir + fileName).toString().replace("$doSpaceBucket.$doSpaceRegion.$doSpaceEndpoint", doChangeDns.toString())
    }

    @Throws(IOException::class)
    fun delete(filename: String, dir: String) {
        s3Client.deleteObject(DeleteObjectRequest(doSpaceBucket, dir + filename))
    }

}