import xml.etree.ElementTree as ET
import re

tree = ET.parse('p21.xml') # Set this
root = tree.getroot()

xmlStringIn = ET.tostring(root).decode('utf-8')

print(xmlStringIn)

flop = xmlStringIn

print(flop)


REG_EX = r'<XRef.*?>'
pattern = re.compile(REG_EX)
flip = pattern.sub('', flop)

REG_EX = r'</XRef.*?>'
pattern = re.compile(REG_EX)
flop = pattern.sub('', flip)

REG_EX = r'<DefinedTerm.*?>'
pattern = re.compile(REG_EX)
flip = pattern.sub('', flop)

REG_EX = r'</DefinedTerm.*?>'
pattern = re.compile(REG_EX)
flop = pattern.sub('', flip)

REG_EX = r'<Emph.*?>'
pattern = re.compile(REG_EX)
flip = pattern.sub('', flop)

REG_EX = r'</Emph.*?>'
pattern = re.compile(REG_EX)
flop = pattern.sub('', flip)

REG_EX = r'<Langu.*?>'
pattern = re.compile(REG_EX)
flip = pattern.sub('', flop)

REG_EX = r'</Langu.*?>'
pattern = re.compile(REG_EX)
flop = pattern.sub('', flip)


print(flop)

# treeOut = ET.ElementTree(ET.fromstring(flop));

text_file = open("p21stripped.xml", "w")
text_file.write(flop)
text_file.close()
