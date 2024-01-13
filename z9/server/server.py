from flask import Flask, jsonify,request
import json
import stripe
from datetime import datetime
app = Flask(__name__)

payments = []
stripe_server_key = "sk_test_51OXi1KEbgHp6w50Cuz2S7muZ8ySpDcd8lfDBD6F9d4obDI0NQViAKFlrUbajRdhNQfs4ub2E1eS63Se6yQOe1C7L007Ja4RuJ8"
stripe_public_key = "pk_test_51OXi1KEbgHp6w50CbfbIR9Kxv2vkIVI3quFOTlqAcaTWL1BUxBI1HYj7AvmUXNUi9ZEcqPZTyov5caxZ6ATeXEJb006fZm28H8"

@app.route("/test")
def test():
	res={"a":"b"}
	return jsonify(json.dumps(res))

@app.route("/mpay",methods=['GET','POST'])
def pay():
	try:
		payments.append({
			'name':request.json['name'],
			'price':float(request.json['price']),
			'date':str(datetime.now())
			})
		return "generic response body"
	except:
		pass
	return '',400
@app.route("/history")
def history():
	return jsonify(payments)
@app.route("/stripe/<amount>",methods=['POST'])
def payStripe(amount):
	global stripe_server_key,stripe_public_key
	stripe.api_key = stripe_server_key
	customer = stripe.Customer.create()
	ephemeralKey = stripe.EphemeralKey.create(
		customer=customer['id'],
		stripe_version='2023-10-16',
  	)
	paymentIntent = stripe.PaymentIntent.create(
		amount=int(amount),
		currency='pln',
		customer=customer['id'],
		automatic_payment_methods={
		  'enabled': True,
		},
  	)
	return jsonify(paymentIntent=paymentIntent.client_secret,
					ephemeralKey=ephemeralKey.secret,
					customer=customer.id,
					publishableKey=stripe_public_key)

if __name__ == '__main__':
	print("t")
	app.run(host="0.0.0.0")