package com.bridgelabz.fundoonotes.utility;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;

@Component
public class Jwt {
	
	private static final String secretkey = "1234567890";
	
	public String createToken(long l)
	{
		Algorithm algorithm = Algorithm.HMAC256(secretkey);
	
		return JWT.create()
				.withClaim("email",l)
				.sign(algorithm);
	}
	
	public long parseJwtToken(String token)
	{
		/**
		 * The Claim class holds the value in a generic way so that it can be recovered in many representations.
		 */
		Claim claim = JWT.require(Algorithm.HMAC256(secretkey))
				.build()
				.verify(token)
				.getClaim("email");
		return claim.asLong();
	}
	
}
