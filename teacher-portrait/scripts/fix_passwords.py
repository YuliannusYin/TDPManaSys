import bcrypt
import pymysql

password = b"123456"
hash_bytes = bcrypt.hashpw(password, bcrypt.gensalt(rounds=10))
hash_str = hash_bytes.decode()

print(f"Generated BCrypt hash: {hash_str}")
print(f"Verify: {bcrypt.checkpw(password, hash_str.encode())}")
assert bcrypt.checkpw(password, hash_str.encode()), "Hash verification FAILED!"

conn = pymysql.connect(host="localhost", port=3308, user="root", password="root",
                       database="teacher_portrait", charset="utf8mb4", autocommit=True)
cur = conn.cursor()
cur.execute("UPDATE user SET password = %s WHERE work_no = 'admin'", (hash_str,))
cur.execute("UPDATE user SET password = %s WHERE work_no = 'T001'", (hash_str,))
cur.execute("SELECT work_no, LEFT(password, 12) AS pwd_prefix FROM user")
for row in cur.fetchall():
    print(f"  {row[0]}: {row[1]}...")
cur.close()
conn.close()

print(f"\nUse this hash in SQL files:")
print(hash_str)