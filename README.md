# レストラン予約システム - RestaurantSystem

Java + Spring Bootで構築した、飲食店向けの予約・メニュー管理システムです。

## 概要

このアプリケーションは、レストランの以下の機能を提供します：

- **メニュー管理**：管理者がメニュー項目（料理名、価格）を登録・編集・削除
- **予約管理**：一般ユーザーが予約を入力、管理者が予約一覧を確認
- **ユーザー管理**：管理者が店舗スタッフユーザーを作成・編集・削除
- **セキュリティ**：Spring Securityによるログイン認証・アクセス制限

---

## システム要件

- **Java**：JDK 17.x（Amazon Corretto推奨）
- **Maven**：3.6以上
- **OS**：Windows / macOS / Linux

---

## セットアップ手順

### 1. リポジトリのクローン

```bash
git clone <リポジトリURL>
cd RestaurantSystem
```

### 2. 依存関係のインストール

```bash
./mvnw clean install
```

（Windows PowerShellの場合）
```powershell
.\mvnw.cmd clean install
```

---

## 実行方法

### アプリケーション起動

```bash
./mvnw spring-boot:run
```

（Windows PowerShellの場合）
```powershell
.\mvnw.cmd spring-boot:run
```

### ブラウザでアクセス

- **トップページ**：http://localhost:8080/
- **ログイン画面**：http://localhost:8080/login
- **H2 Console**：http://localhost:8080/h2-console

---

## 初期ユーザー情報

アプリケーション起動時に、初期管理者ユーザーが自動作成されます。

| ユーザー名 | パスワード | ロール |
|-----------|-----------|--------|
| `admin`   | `adpass`  | ROLE_ADMIN |

### H2 Consoleへのアクセス

1. ブラウザで http://localhost:8080/h2-console にアクセス
2. 以下の情報を入力：
   - **JDBC URL**：`jdbc:h2:file:./data/restaurant_db`
   - **User Name**：`sa`
   - **Password**：（空白）
3. 「Connect」をクリック

---

## 機能説明

### 一般ユーザー（ログインなし）

| ページ | URL | 説明 |
|--------|-----|------|
| トップページ | `/` | メニュー、予約へのリンク表示 |
| メニュー一覧 | `/menu` | 登録されているメニューを表示 |
| 予約フォーム | `/reservation` | 予約日時、名前、人数、連絡先を入力 |
| 予約完了画面 | `/reservation/success` | 予約完了メッセージ表示 |

### 管理者（ログイン必須：`admin` / `adpass`）

| ページ | URL | 説明 |
|--------|-----|------|
| ダッシュボード | `/admin/index` | メニュー、予約、ユーザー管理へのリンク |
| メニュー一覧 | `/admin/menu` | メニューの一覧表示・追加 |
| メニュー追加フォーム | `/admin/menu/add` | 料理名と価格を入力して登録 |
| 予約一覧 | `/admin/reservation` | 顧客から受け付けた予約を確認 |
| ユーザー一覧 | `/admin/users` | 店舗スタッフユーザーの一覧表示 |
| ユーザー追加フォーム | `/admin/users/add` | 新規ユーザー（スタッフ）を作成 |
| ユーザー編集フォーム | `/admin/users/edit/{id}` | ユーザー情報を編集 |

---

## フォルダ構造

```
RestaurantSystem/
├── src/
│   ├── main/
│   │   ├── java/com/Restaurant/RestaurantSystem/
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java           # セキュリティ設定
│   │   │   ├── controller/
│   │   │   │   ├── AdminMenuController.java      # メニュー管理（管理者）
│   │   │   │   ├── AdminReservationController.java # 予約一覧（管理者）
│   │   │   │   ├── AdminUserController.java      # ユーザー管理（管理者）
│   │   │   │   ├── PublicMenuController.java     # メニュー表示（公開）
│   │   │   │   └── PublicReservationController.java # 予約入力（公開）
│   │   │   ├── entity/
│   │   │   │   ├── Menu.java                     # メニューエンティティ
│   │   │   │   ├── Reservation.java              # 予約エンティティ
│   │   │   │   └── User.java                     # ユーザーエンティティ
│   │   │   ├── repository/
│   │   │   │   ├── MenuRepository.java
│   │   │   │   ├── ReservationRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── service/
│   │   │   │   ├── MenuService.java
│   │   │   │   ├── ReservationService.java
│   │   │   │   ├── UserService.java
│   │   │   │   └── CustomUserDetailsService.java # Spring Security用
│   │   │   └── RestaurantSystemApplication.java  # ブートストラップクラス
│   │   └── resources/
│   │       ├── application.yaml                  # アプリケーション設定
│   │       ├── static/
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   └── images/
│   │       └── templates/
│   │           ├── hello.html                    # トップページ
│   │           ├── login.html                    # ログイン画面
│   │           ├── admin/
│   │           │   ├── index.html                # ダッシュボード
│   │           │   ├── menu-list.html
│   │           │   ├── menu-form.html
│   │           │   ├── reservation-list.html
│   │           │   ├── user-list.html
│   │           │   └── user-form.html
│   │           └── public/
│   │               ├── menu-list.html
│   │               ├── reservation-form.html
│   │               └── reservation-success.html
│   └── test/
│       └── java/...                              # ユニットテスト（今後拡張）
├── data/
│   └── restaurant_db.mv.db                       # H2 Databaseファイル
├── pom.xml                                       # Maven設定
├── mvnw / mvnw.cmd                              # Maven Wrapper
├── .gitignore                                    # Git設定
└── README.md                                     # このファイル
```

---

## 技術スタック

| 技術 | バージョン | 目的 |
|------|-----------|------|
| Spring Boot | 3.5.10 | フレームワーク |
| Spring Web | - | RESTful API & MVC |
| Spring Security | - | 認証・認可 |
| Spring Data JPA | - | ORM（データベース操作） |
| Thymeleaf | - | サーバーサイドテンプレートエンジン |
| H2 Database | - | インメモリ＆ファイルベースDB |
| Lombok | - | ボイラープレートコード削減 |
| Validation | - | 入力値検証 |
| Java | 17 LTS | プログラミング言語 |

---

## よくある質問（FAQ）

### Q1. アプリが起動しない

**A.** 以下を確認：
- Java 17がインストール済み：`java -version`
- Maven 3.6以上がインストール済み：`mvn -version`
- ポート8080が使用されていないか確認
- `mvnw clean compile`を実行してコンパイルエラーを確認

### Q2. ログインができない

**A.** 初期認証情報を確認：
- ユーザー名：`admin`
- パスワード：`adpass`
- 大文字小文字は区別されます

### Q3. H2 Consoleにアクセスできない

**A.** JDBC URLが正しいか確認：
```
jdbc:h2:file:./data/restaurant_db
```
相対パスはプロジェクトルートからの相対位置です。

### Q4. メニューや予約が保存されない

**A.** H2 Consoleでテーブルを確認：
```sql
SELECT * FROM restaurant_menu;
SELECT * FROM reservation;
SELECT * FROM users;
```

---

## ライセンス

このプロジェクトはポートフォリオ用途です。

---

## 連絡先 / サポート

問題が発生した場合、以下をご確認ください：
- コンソールログでエラーメッセージを確認
- `data/restaurant_db.mv.db`ファイルが存在するか確認
- `.gitignore`に登録されたファイルを誤削除していないか確認

---

## 更新履歴

- **v1.0.0（2026-02-21）**
  - 初版リリース
  - メニュー管理、予約管理、ユーザー管理機能実装
  - Spring Security統合、ログイン認証実装
