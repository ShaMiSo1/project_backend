package com.example.bangyo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BangyoApplication {

	public static void main(String[] args) {
		// 1) .env 파일 로드 → "JWT_SECRET_KEY" 읽기
		Dotenv dotenv = Dotenv.configure()
				.directory("./")
				.ignoreIfMalformed()
				.ignoreIfMissing()
				.load();

		String secretKey = dotenv.get("JWT_SECRET_KEY");
		if (secretKey != null) {
			// 2) Spring 컨텍스트가 생성되기 전에 System Property 세팅
			System.setProperty("JWT_ENV_KEY", secretKey);
			System.out.println("✅ .env에서 JWT_SECRET_KEY 로드 후 JWT_ENV_KEY로 설정");
		} else {
			System.out.println("⚠️ .env에서 JWT_SECRET_KEY를 찾을 수 없습니다.");
		}

		// 3) 스프링 애플리케이션 실행
		SpringApplication.run(BangyoApplication.class, args);
	}
}
