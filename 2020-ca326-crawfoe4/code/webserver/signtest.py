def verify_sign(pub_key, signature, data):
    print("pub_key:", pub_key, "signature:", signature, "data:", data)
    from Crypto.PublicKey import RSA
    from Crypto.Signature import PKCS1_v1_5
    from Crypto.Hash import SHA256
    from base64 import b64decode, b64encode
    import json
    print("Signature:", signature)
    print("Data:", data)
    print(type(data))
    decodedkey = b64decode(pub_key+"===")
    rsakey = RSA.importKey(decodedkey)
    signer = PKCS1_v1_5.new(rsakey)
    digest = SHA256.new()
    #encoded = b64encode(data.encode("utf-8"))
    #data = json.load(data)
    #data = data.encode()
    digest.update(data)
    print(signer.verify(digest, b64decode(signature)))
    if signer.verify(digest, b64decode(signature+"===")):
       return True
    return False


def main():
	print(verify_sign("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmrsK6rkEtWMlVN9EvNaI4EAXwGKoPCCIWSgLjL7AUFYNCm91VjvdgNqzluEQ7kvYgAmgCplpMZz8BV9vJdAfWK/3KaTPAIpEBA2PbwsCWiWv6LDvRY68VBkn425PdjvGZk7ifgAC0Fj26xGuCTWAxO0loHkueYnjqfGw9PSZ82HkH003142YrrvsLCXgfs8qFfZx6XJJnHbZ8kMpZ92AVyxS2zxKW+dE8YLnFDcA5hS3bz2EhIXMXsXT8J2p67hYi74fISNfmqHt7chfGdZxpNm+Tc/m9qQdGQmD1OpE26rfoR9yabdaQmBdfTL2epEqzfURLETP0K8D26uG2LBemwIDAQAB","SiGbj8yFqLiwFBCC8uZE6Y0QPSR67CiXyZslidRy8Qvh/8NFLNJqpgFKTUiwS3AtgsPmDXTwIpbfzDjG5PqrmZg+9LUAMy4CD7czqQyS5ObN6n/aL05SHh1V6tFGOSpCtzAjxL0VwS4YVAIB8vE1Fx474KrUSxWWE18NdL3JjiI83Vuq3aEsxeNgw9ACnRv1Y9aeaeatviLGJShrFthwJJU3fyxkE8Diz9JW+IzDkIFC0FlcNkFGWSOHSd5fdZ/UiazgGFAwerqMomVmZIfYwusruaFi6feSrvOee+aZK1TWlX7/aA21OWnjlolPj5HfJMR+AMlV9G5LN4FKds5pog==",""))
	print("Shouldn")

if __name__ == '__main__':
	main()
