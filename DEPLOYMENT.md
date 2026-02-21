# デプロイ準備ガイド - RestaurantSystem

本番環境へのデプロイ前に確認すべき項目とデプロイ手順をまとめています。

---

## デプロイ前チェックリスト

### セキュリティ確認

- [ ] 初期管理者パスワード（`adpass`）を本番環境用に変更開始後に`/admin/users`から新しいパスワードで管理者を作成し、デフォルト管理者は削除
- [ ] `application.yaml`にハードコードされた設定を環境変数に移行
  - データベース接続設定
  - Spring Securityのシークレットキー
- [ ] JPA設定を確認：`ddl-auto: update`は本番では`validate`または`none`に変更推奨
- [ ] H2 Consoleを本番では無効化（`spring.h2.console.enabled: false`）
- [ ] CORS設定を必要に応じて追加

### 動作確認

- [ ] 開発環境で全機能をテスト完了
  - メニュー登録・編集・削除
  - 予約入力
  - ユーザー管理（作成・編集・削除）
  - ログイン・ログアウト
- [ ] バリデーション確認
  - 空フィールドでエラーが出るか
  - 不正なメールアドレスでエラーが出るか
  - 過去日時での予約ができないか

### パフォーマンス確認

- [ ] 大量データ（数千件のメニュー・予約）での動作確認
- [ ] データベースクエリの確認（`show-sql: true`でコンソール出力）
- [ ] ログレベルを`INFO`以上に設定（開発時の`DEBUG`は本番では不要）

---

## デプロイ手順

### 1. アプリケーション設定の環境別対応

#### 開発環境（`application-dev.yaml`）

```yaml
spring:
  application:
    name: RestaurantSystem
  datasource:
    url: jdbc:h2:file:./data/restaurant_db
    driver-class-name: org.h2.Driver
    username: sa
    password:
  h2:
    console:
      enabled: true
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: Asia/Tokyo

logging:
  level:
    root: INFO
    com.Restaurant.RestaurantSystem: DEBUG
```

#### 本番環境（`application-prod.yaml`）

```yaml
spring:
  application:
    name: RestaurantSystem
  datasource:
    url: ${DB_URL}
    driver-class-name: org.h2.Driver
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  h2:
    console:
      enabled: false
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: Asia/Tokyo

logging:
  level:
    root: WARN
    com.Restaurant.RestaurantSystem: INFO
```

### 2. ビルド

```bash
./mvnw clean package
```

出力：`target/RestaurantSystem-<version>.jar`

### 3. JAR実行（本番環境）

```bash
java -jar target/RestaurantSystem-<version>.jar \
  --spring.profiles.active=prod \
  --server.port=8080 \
  -DAPP_ENV=production
```

または環境変数経由：

```bash
export SPRING_PROFILES_ACTIVE=prod
export DB_URL=jdbc:h2:file:/var/lib/restaurant/data/restaurant_db
export DB_USERNAME=admin_db
export DB_PASSWORD=<strong-password>

java -jar target/RestaurantSystem-<version>.jar
```

---

## デプロイ環境別推奨設定

### オンプレミス / VPS

```bash
# systemd サービスファイル例：/etc/systemd/system/restaurant.service
[Unit]
Description=Restaurant Reservation System
After=network.target

[Service]
Type=simple
User=restaurant
WorkingDirectory=/opt/restaurant
ExecStart=/usr/bin/java -jar /opt/restaurant/RestaurantSystem.jar \
  --spring.profiles.active=prod
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

起動・停止：
```bash
sudo systemctl start restaurant
sudo systemctl stop restaurant
sudo systemctl status restaurant
```

### Docker（推奨）

#### Dockerfile

```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/RestaurantSystem-*.jar app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=prod

CMD ["java", "-jar", "app.jar"]
```

#### docker-compose.yml

```yaml
version: '3.8'

services:
  restaurant-app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DB_URL=jdbc:h2:file:/data/restaurant_db
      - DB_USERNAME=sa
      - DB_PASSWORD=
    volumes:
      - restaurant-data:/data
    restart: always

volumes:
  restaurant-data:
```

ビルド・実行：
```bash
docker-compose up -d
```

### クラウド（Azure / AWS）

#### Azure App Service

1. リソース作成：
```bash
az appservice plan create --resource-group myGroup --name myPlan --sku B1
az webapp create --resource-group myGroup --plan myPlan --name restaurant-app --runtime "java|17-java17"
```

2. JAR デプロイ：
```bash
az webapp deployment source config-zip --resource-group myGroup --name restaurant-app --src <jar-file>.zip
```

#### AWS Elastic Beanstalk

1. EB初期化：
```bash
eb init -p java-17 restaurant-app
```

2. デプロイ：
```bash
eb create restaurant-env
eb deploy
```

---

## データベース移行

### H2からPostgreSQLへの移行例

#### 1. 依存関係追加（`pom.xml`）

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.1</version>
</dependency>
```

#### 2. 接続設定（`application-prod.yaml`）

```yaml
spring:
  datasource:
    url: jdbc:postgresql://db-host:5432/restaurant_db
    driver-class-name: org.postgresql.Driver
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
```

#### 3. マイグレーション

```bash
./mvnw clean package
java -jar target/RestaurantSystem-*.jar --spring.profiles.active=prod
```

JPAが自動スキーマ生成（`ddl-auto: update`）

---

## トラブルシューティング

### エラー：`Could not connect to database`

**確認事項：**
- DB_URLが正しいか
- DB_USERNAMEとDB_PASSWORDが正しいか
- ファイアウォール設定でポートが開いているか

### エラー：`H2 Console is not available`

**本番環境での挙動（意図的）：**
```yaml
spring.h2.console.enabled: false
```

開発環境での確認は別の環境を用意

### ログを確認したい

```bash
# リアルタイム確認
docker-compose logs -f restaurant-app

# またはシステムログ
journalctl -u restaurant -f
```

---

## 監視・ログ設定

### Spring Actuator有効化（推奨）

`pom.xml`に追加：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

`application-prod.yaml`：
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: when-authorized
```

ヘルスチェック：`http://<host>:8080/actuator/health`

### ログ設定

```yaml
logging:
  level:
    root: WARN
    com.Restaurant.RestaurantSystem: INFO
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: /var/log/restaurant/app.log
    max-size: 10MB
    max-history: 30
```

---

## 本番環境での最終確認

```bash
# 1. ヘルスチェック
curl http://localhost:8080/actuator/health

# 2. ログイン確認
# ブラウザで http://localhost:8080/login にアクセス

# 3. メニュー一覧確認
# http://localhost:8080/menu

# 4. 予約機能確認
# http://localhost:8080/reservation で予約入力テスト

# 5. 管理画面確認
# ログイン後http://localhost:8080/admin/users で管理者ユーザーを追加
```

---

## ロールバック手順

本番環境で問題が発生した場合：

```bash
# 前のバージョンを実行
java -jar RestaurantSystem-<previous-version>.jar --spring.profiles.active=prod

# またはDocker環境
docker-compose down
git checkout <previous-commit>
docker-compose up -d
```

---

## まとめ

デプロイ前に以下を実施し、本番環境で安全に運用できる状態を確保してください：

1. **セキュリティ**：デフォルトパスワード変更、H2 Console無効化
2. **設定**：環境変数による動的設定
3. **テスト**：事前に全機能確認
4. **監視**：ログ・メトリクス設定
5. **バックアップ**：本番DBの定期バックアップ

質問や問題がある場合は、GitHubのIssueを作成してください。
