package com.example.bangyo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import jakarta.annotation.PostConstruct;  // ✅ javax -> jakarta 로 변경

@SpringBootApplication
public class BangyoApplication {

	@PostConstruct
	public void loadEnvVariables() {
		// 1) .env 파일 로드
		Dotenv dotenv = Dotenv.configure()
				.directory("./")
				.ignoreIfMalformed()
				.ignoreIfMissing()
				.load();

		// 2) JWT_SECRET_KEY 가져오기
		String secretKey = dotenv.get("JWT_SECRET_KEY");

		// 3) 값이 있으면 System Property에 설정
		if (secretKey != null) {
			System.setProperty("jwt.secretKey", secretKey);
			System.out.println("✅ .env에서 JWT_SECRET_KEY가 로드되었습니다.");
		} else {
			System.out.println("⚠️ .env에서 JWT_SECRET_KEY를 찾을 수 없습니다.");
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(BangyoApplication.class, args);
	}
}
