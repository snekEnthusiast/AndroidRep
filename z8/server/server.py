from flask import Flask
app = Flask(__name__)

accounts = {
	"admin" : "admin",
	"a":"a"
} #read/write from dict for ease of testing
	
content = "totally secret login-gated content"

@app.route("/test")
def test():
	return "hello"

@app.route("/account/<username>/<password>")
def login(username,password):
	if username in accounts:
		if accounts[username] == password:
			return content
	return 'wrong password',403

@app.route("/register/<username>/<password>")
def create_account(username,password):
	if not username in accounts:
		accounts[username] = password
		return '', 200
	return 'already exists',400

if __name__ == '__main__':
	print("t")
	app.run(host="0.0.0.0")