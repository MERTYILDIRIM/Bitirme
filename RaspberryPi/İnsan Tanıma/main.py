import cv2
import glob
import os
import time
import imutils
import argparse
import serial
from imutils.object_detection import non_max_suppression

subject_label = 1
total_count = 0
subject_one_count = 0
font = cv2.FONT_HERSHEY_SIMPLEX
list_of_videos = []
cascade_path = "face_cascades/haarcascade_profileface.xml"
face_cascade = cv2.CascadeClassifier(cascade_path)
hog = cv2.HOGDescriptor()
hog.setSVMDetector(cv2.HOGDescriptor_getDefaultPeopleDetector())
serPort = 0
##time.sleep(60)
##recognizer = cv2.face.createLBPHFaceRecognizer()
while True:
    try:
        ser = serial.Serial('/dev/ttyACM'+str(serPort),9600)
        break
    except:
        serPort = serPort + 1
        serPort = serPort % 4
        continue


def detect_people(frame):
	"""
	detect humans using HOG descriptor
	Args:
		frame:
	Returns:
		processed frame
	"""
	(rects, weights) = hog.detectMultiScale(frame, winStride=(4, 4), padding=(16, 16), scale=1.06)
	rects = non_max_suppression(rects, probs=None, overlapThresh=0.65)
	for (x, y, w, h) in rects:
		cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 0, 255), 2)
	return frame, len(rects)



def detect_face(frame):
	"""
	detect human faces in image using haar-cascade
	Args:
		frame:
	Returns:
	coordinates of detected faces
	"""
	faces = face_cascade.detectMultiScale(frame)
	return faces


def recognize_face(frame_orginal, faces):
	"""
	recognize human faces using LBPH features
	Args:
		frame_orginal:
		faces:
	Returns:
		label of predicted person
	"""
	predict_label = []
	predict_conf = []
	for x, y, w, h in faces:
		frame_orginal_grayscale = cv2.cvtColor(frame_orginal[y: y + h, x: x + w], cv2.COLOR_BGR2GRAY)
		cv2.imshow("cropped", frame_orginal_grayscale)
		predict_tuple = recognizer.predict(frame_orginal_grayscale)
		a, b = predict_tuple
		predict_label.append(a)
		predict_conf.append(b)
		print(predict_tuple)
	return predict_label


def draw_faces(frame, faces):
	"""
	draw rectangle around detected faces
	Args:
		frame:
		faces:
	Returns:
	face drawn processed frame
	"""
	for (x, y, w, h) in faces:
		xA = x
		yA = y
		xB = x + w
		yB = y + h
		cv2.rectangle(frame, (xA, yA), (xB, yB), (0, 255, 0), 2)
	return frame


def put_label_on_face(frame, faces, labels):
	"""
	draw label on faces
	Args:
		frame:
		faces:
		labels:
	Returns:
		processed frame
	"""
	i = 0
	for x, y, w, h in faces:
		cv2.putText(frame, str(labels[i]), (x, y), font, 1, (255, 255, 255), 2)
		i += 1
	return frame


if __name__ == '__main__':
	"""
	main function
	"""
	##ap = argparse.ArgumentParser()
	##ap.add_argument("-v", "--videos", required=True, help="path to videos directory")
	##args = vars(ap.parse_args())
	##path = args["videos"]
	##for f in os.listdir(path):
        if True:
		##list_of_videos = glob.glob(os.path.join(os.path.abspath(path), f) + "/*.mp4")
		##print(os.path.join(os.path.abspath(path), f) + "*.mp4")
		##print(list_of_videos)
		if os.path.exists("model.yaml"):
			##recognizer.load("model.yaml")
			##for video in list_of_videos:
                        while True:
				print("hello")
				##camera = cv2.VideoCapture(os.path.join(path, video))
				while True:
					starttime = time.time()
					##grabbed, frame = camera.read()
					frame  = cv2.imread("../frame.png")
					print("GET FRAME")
					##if not grabbed:
					##	break
					frame_orginal = imutils.resize(frame, width=min(500, frame.shape[1]))
					frame_orginal1 = frame_orginal##cv2.cvtColor(frame_orginal, cv2.COLOR_BGR2GRAY)
					frame_processed, humans = detect_people(frame_orginal1)
                                        print("FRAME PROCESS COMPLETE")
					faces = detect_face(frame_orginal)
					##if len(faces) > 0:
					if humans > 0:
                                                print("Human Detected")
                                                ser.write('2\n')
						cv2.imwrite("human.png", frame_processed) 
                                                ##frame_processed = draw_faces(frame_processed, faces)
						##label = recognize_face(frame_orginal, faces)
						##frame_processed = put_label_on_face(frame_processed, faces, label)
						##for i in label:
						##	total_count = total_count + 1
						##	if i == 1:
						##		subject_one_count = subject_one_count + 1
					##cv2.imshow("window", frame_processed)
                                        key = cv2.waitKey(1) & 0xFF
					if key == ord("q"):
						break
				##camera.release()
				cv2.destroyAllWindows()
				endtime = time.time()
		else:
			print("model file not found")
		list_of_videos = []
