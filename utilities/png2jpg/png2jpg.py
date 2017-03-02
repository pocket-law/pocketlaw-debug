import Image
import os

increment = 0

for filename in os.listdir('.'):
    if filename.endswith(".png"): 
        increment += 1
		
        print("here's one: " + os.path.join(filename))
	  
        im = Image.open(filename)
        im.save('screenshot' + str(increment) + '.jpg')
		
        continue
    else:
        continue