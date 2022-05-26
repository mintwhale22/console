package kr.mintwhale.console.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class DoConfig {

    var log = LoggerFactory.getLogger(this::class.java) as Logger

    @Value("\${do.space.key}")
    private val doSpaceKey: String? = null

    @Value("\${do.space.secret}")
    private val doSpaceSecret: String? = null

    @Value("\${do.space.endpoint}")
    private val doSpaceEndpoint: String? = null

    @Value("\${do.space.region}")
    private val doSpaceRegion: String? = null

    @Bean
    fun getS3(): AmazonS3? {
        val creds = BasicAWSCredentials(doSpaceKey, doSpaceSecret)
        return AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(EndpointConfiguration("$doSpaceRegion.$doSpaceEndpoint", doSpaceRegion))
            .withCredentials(AWSStaticCredentialsProvider(creds)).build()
    }

}
