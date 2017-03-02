import xml.etree.ElementTree as ET
import re


# Set input and output file names
inputFile = 'I-3.3.xml'	 
outputFile = 'stripped_test.xml'

tree = ET.parse(inputFile) 
root = tree.getroot()

xmlStringIn = ET.tostring(root).decode('utf-8')

# flip and flop to pass string back and forth
flop = xmlStringIn

print("Stripping " + inputFile + "...")

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

REG_EX = r'<Repea.*?>'
pattern = re.compile(REG_EX)
flip = pattern.sub('', flop)

REG_EX = r'</Repea.*?>'
pattern = re.compile(REG_EX)
flop = pattern.sub('', flip)

print("Strip Complete!")

text_file = open(outputFile, "w")
text_file.write(flop)
text_file.close()

print("Saved to: " + outputFile)
