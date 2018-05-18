About Picture Generating Extra Credit on Markov Model

Summary:
Hi everyone, as mentioned on the TA meeting, I tried applying the
idea of mapping a prefix to the suffix to picture.
This would be a doable extra credit but it may contain too many
things to be done if we provide no hints/starter codes.
Though with the idea of machine learning, Markov Model may not be as
applicable as in dealing texts.
And always, the biggest problem for machine leanrning related program
is that we should at least try providing students with standardized
data pictures to train the model they build.


Classes:
Picture class: The picture processing class I use is downloaded from:
https://introcs.cs.princeton.edu/java/stdlib/Picture.java.html
It's a little inconvient because it treats RGB as a whole integer but
basic functions are already included.
Pixel class: I briefly implement it based on the functions provided in
Picture class. Pixel class enables get R,G,B values independently.

PictureGenerator class: This class is equivalent to the TextGenerator.
It is consisted of a 2D array of PixelGenerator. PictureGenerator may
take in a picture to "train" all the PixelGenerator inside itself.
After training, it may output its own predicted picture.
PixelGenerator class: This class contains all "weights" required to
predict a new Pixel. It may be trained by comparing prediced Pixel
with correct Pixel to adjust its own parameters.


Difference between text Markov Model and Picture:
Pixels' identity--RGB values have weigh more combinations than that of
words/characters. If we directly shift the idea of text markov model
to pixels, each pixel could be treated as a new key because highly
possibly every one is different. If we are using grey-level picture which
largely decreases the freedom degree, the output would probably just be
scatted dots.

Therefore, I think it's better to use digital calculatoins to predict
next Pixel's RGB values based on prefix pixels. The coefficients(weights)
will be trained by many pictures.
Furthermore, I think it might be more useful if these coefficients are
binded with their location on the picture. Else the result after training
pixel by pixel does not make much sense.


Mechanism:
A Pixel is predicted by PixelGenerator based on 3 Pixels: Pixel on the
left, Pixel at the top and Pixel to the upperLeft. Therefore, there are
3*3*3=27 parameters to be trained per PixelGenerator.
Each input Pixel is matched with a 3*3 matrix A, thus
predictedRGB = A*inputRGB
The final result would be combinations of 3 predictedRGBs.
These weights (entities in the matrix) are supposed to be double type
between 0 and 1 but it's acceptable if the value exceeds.
During the training, the method would compare predicted value with true
value and new weight would be given by formula:
w_new = w_old - diff_rate*(r,g,b)
diff_rate is the error rate between predicted value and true value. It
would be timed by corresponding r/g/b value because the higher the r/g/b
value is, the more important this weight is when determing next pixel.

As Extra Credit:
To make the task within reasonable length as an extra credit,
1.
Picture and Pixel class may be provided so that students may process
images.
2.
Hints may be given on the mechanism above. We can say that this is just
one of the possible ways because students may have their own ideas of
how to train their models.
3.
Combine current model with HashMap. Currently in my implementation,
students get exercises on array manipulation but not quite related to
HashMap used in the previous part of this PSA. We could therefore
modify the PixelGenerator class to make it become a HashMap based object.
4.
Data sources. In the example given, I only use 1 picture to train the
model. If more "similar" pictures are given, the model may get able to
paint based on the style of all pictures.
For example, suppose the model is trained based on many human's faces.
Ideally the model would be able to draw a face-like shape with similar
color changing after training.
I could try to find dozens of faces and clip them into same dimensions
later if needed.



