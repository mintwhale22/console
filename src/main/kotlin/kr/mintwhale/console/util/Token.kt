package kr.mintwhale.console.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import kr.mintwhale.console.config.DefaultConfig
import kr.mintwhale.console.data.model.Member
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class Token {
    var log = LoggerFactory.getLogger(this::class.java) as Logger

    private val DAY = 3600L * 24

    fun make(member: Member): String {
        return try {
            JWT.create()
                .withIssuer(DefaultConfig.TOKEN_ISSUER)
                .withClaim("email", member.strEmail)
                .withClaim("name", member.strName)
                .withClaim("nik", member.strNikname)
                .withClaim("rand", Date().time + Etc.randomRange(111, 999))
                .withExpiresAt(Date.from(LocalDateTime.now().toInstant(ZoneOffset.ofHours(9))))
                .sign(Algorithm.HMAC256(DefaultConfig.TOKEN_KEY))
        } catch (e: Exception) {
            ""
        }
    }

    fun get(value: String, day: Int = 30): Member? {
        try {
            val member = Member()

            val verifier = JWT.require(Algorithm.HMAC256(DefaultConfig.TOKEN_KEY))
                .withIssuer(DefaultConfig.TOKEN_ISSUER)
                .acceptExpiresAt(DAY * day) // 만료일 -{day}일
                .build()

            val jwt = verifier.verify(value)

            member.strEmail = jwt.getClaim("email").asString()
            member.strName = jwt.getClaim("name").asString()
            member.strNikname = jwt.getClaim("nik").asString()

            return member
        } catch (e: Exception) {
            return null
        }
    }
}