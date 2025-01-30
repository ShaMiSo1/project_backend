package com.example.bangyo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BangyoApplication {

	public static void main(String[] args) {
		// .env 파일 로드 → "JWT_SECRET_KEY" 등 읽기
		Dotenv dotenv = Dotenv.configure()
				.directory("./") // .env 파일이 프로젝트 루트에 있는지 확인
				.ignoreIfMalformed()
				.ignoreIfMissing()
				.load();

		// .env 파일의 모든 키-값을 시스템 속성에 설정
		dotenv.entries().forEach(entry -> {
			System.setProperty(entry.getKey(), entry.getValue());
		});

		// Spring Boot 애플리케이션 실행
		SpringApplication.run(BangyoApplication.class, args);
	}
}
