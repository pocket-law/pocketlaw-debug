import xml.etree.ElementTree as ET
import re


# Set input and output file names
inputFile = 'p21.xml'	 
outputFile = 'p21stripped.xml'

tree = ET.parse(inputFile) 
root = tree.getroot()

xmlStringIn = ET.tostring(root).decode('utf-8')

# flip and flop to pass string back and forth
flop = xmlStringIn

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

print("Strip Complete!")

text_file = open(outputFile, "w")
text_file.write(flop)
text_file.close()
