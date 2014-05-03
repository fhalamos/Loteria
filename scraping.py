import random
import urllib.request
responses = set()
i = 0
while(i < 1000):
    codigo = random.randint(3460000000000, 3461000000000)
    response = urllib.request.urlopen('http://www.loteria.cl/KinoASP/procesa_consulta_kinoP3V2i016CJNK.asp?onHTTPStatus=%5Btype%20Function%5D&Nconsulta=1&panel=0&DV=&Rut=&boleto=0' + str(codigo) + '&sorteo=0').read()
    if response not in responses:
        responses.add(response)
        print(response)
for r in responses:
    print(r)
