package com.example.bangyo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BangyoApplication {

	public static void main(String[] args) {

		// 1) 프로젝트 루트(또는 .env 파일이 있는 디렉토리)에서 .env 읽기
		Dotenv dotenv = Dotenv.configure()
				.directory("./")        // .env가 위치한 경로 (기본 현재 디렉토리)
				.ignoreIfMalformed()    // .env 내용이 잘못되어도 무시
				.ignoreIfMissing()      // .env 파일이 없어도 무시
				.load();

		// 2) .env에서 JWT_SECRET_KEY 가져오기 (예: JWT_SECRET_KEY=...)
		String secretKey = dotenv.get("JWT_SECRET_KEY");

		// 3) 값이 있으면 System Property에 설정 (없으면 null)
		if (secretKey != null) {
			System.setProperty("JWT_SECRET_KEY", secretKey);
		}

		// 4) 이제 스프링부트 애플리케이션 구동
		SpringApplication.run(BangyoApplication.class, args);
	}
}
