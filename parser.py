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
print json
