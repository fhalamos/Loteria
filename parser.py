from operator import methodcaller
f = open('./sample.txt')
lines = f.readlines()[0]
f.close()
lines = lines[1:len(lines)-2]
lines = lines.split('&')
lines = map(methodcaller("split", "="), lines)
json = {}
for line in lines:
    json[line[0]] = line[1]
for k,d in sorted(json.iteritems()):
    print k + ':' + d

#respuesta = 1-> ya hay resultados, 2 -> no se ha efectuado sorteo, hay mensaje
#Fecha = Fecha de compra
#BolKino1 = numeros boleto separados por coma
#SorteoIni,SorteoFin,SorteoUlt = numero de sorteo
#mensaje = solo si sorteo no se ha realizado
#hitsK4Juego1P1 = hits chanchito regalon (numero de coicidencias)
#hitsK41 = hits juego normal
#hitsK4Rev1 = hits rekino
#AKino1 = Aciertos kino normal  de la forma 00/01
#AReKino1 = Aciertos rekino
#AKinoJuego1P1 = Aciertos chanchito regalon
#AKinoCJ2Op11:
#AKinoCJ2Op21:
#AKinoCJ2Op31:
#AKinoCJ2Op41:
#AKinoCJ2Op51:
#AKinoCJOp11:
#AKinoCJOp21:
#AKinoCJOp31:
#AKinoCJOp41:
#AKinoMG1 = Aciertos otros juegos
#Monto1:0
#MontoCJ1:0
#MontoCJ2P1:0
#MontoCon1:0
#MontoGM1:0
#MontoJuego1P1:0 = montos de premios
