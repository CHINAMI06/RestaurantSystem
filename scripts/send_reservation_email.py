#!/usr/bin/env python3
"""
予約完了メールを送信する簡易スクリプト。

使い方（例）:
```bash
python scripts/send_reservation_email.py --to guest@example.com \
    --name "山田 太郎" --datetime "2026-02-28 19:00" --people 2
```

SMTP設定は `scripts/.env` ファイルで管理します。
ファイル形式:
```text
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USER=your@gmail.com
SMTP_PASS=your-app-password
FROM_EMAIL=no-reply@restaurant.com
```

環境変数でオーバーライドすることもできます。

Gmail を使う場合は 2 段階認証を有効にし、
アプリパスワードを発行して `SMTP_PASS` に設定してください。
"""
import os
import argparse
import smtplib
from email.message import EmailMessage
from pathlib import Path

try:
    from dotenv import load_dotenv
    # スクリプトと同じ directory の .env を読み込む
    env_path = Path(__file__).parent / ".env"
    if env_path.exists():
        load_dotenv(env_path)
except ImportError:
    # python-dotenv がない場合はスキップ
    pass


def send_email(smtp_host, smtp_port, smtp_user, smtp_pass, from_addr, to_addr, subject, body, use_tls=True):
    msg = EmailMessage()
    msg["From"] = from_addr
    msg["To"] = to_addr
    msg["Subject"] = subject
    msg.set_content(body)

    if smtp_user and smtp_pass:
        # TLS connection
        server = smtplib.SMTP(smtp_host, smtp_port, timeout=10)
        try:
            if use_tls:
                server.starttls()
            server.login(smtp_user, smtp_pass)
            server.send_message(msg)
        finally:
            server.quit()
    else:
        # No auth, try plain SMTP
        with smtplib.SMTP(smtp_host, smtp_port, timeout=10) as server:
            server.send_message(msg)


def main():
    parser = argparse.ArgumentParser(description="Send reservation confirmation email")
    parser.add_argument("--to", required=True, help="Recipient email address")
    parser.add_argument("--name", required=True, help="Guest name")
    parser.add_argument("--datetime", required=True, help="Reservation datetime")
    parser.add_argument("--people", required=True, help="Number of people")
    parser.add_argument("--message", default="", help="Optional message")

    args = parser.parse_args()

    smtp_host = os.environ.get("SMTP_HOST", "localhost")
    smtp_port = int(os.environ.get("SMTP_PORT", "25"))
    smtp_user = os.environ.get("SMTP_USER")
    smtp_pass = os.environ.get("SMTP_PASS")
    from_email = os.environ.get("FROM_EMAIL", f"no-reply@{os.uname().nodename}") if hasattr(os, 'uname') else os.environ.get("FROM_EMAIL", "no-reply@example.com")

    subject = f"予約が完了しました — {args.name} 様"
    body = (
        f"{args.name} 様\n\n"
        f"ご予約が完了しました。\n"
        f"予約日時: {args.datetime}\n"
        f"人数: {args.people}\n\n"
        f"ご連絡先: {args.to}\n"
    )
    if args.message:
        body += f"\nメッセージ:\n{args.message}\n"

    try:
        send_email(smtp_host, smtp_port, smtp_user, smtp_pass, from_email, args.to, subject, body)
        print("Email sent to", args.to)
    except Exception as e:
        print("Failed to send email:", str(e))


if __name__ == "__main__":
    main()

print("Email sent successfully.")