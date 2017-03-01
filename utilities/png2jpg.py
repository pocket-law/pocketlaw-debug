import Image
import os

for filename in os.listdir('.'):
    if filename.endswith(".png"): 
	
        print("here's one!")
        print(os.path.join(filename))
	  
        im = Image.open(filename)
        im.save('phone1.jpg')
		
        continue
    else:
        continue