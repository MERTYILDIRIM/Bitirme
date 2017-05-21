__author__ = 'jonathandeehan'
#Import serial and time lib

import serial, time, requests
import json
import subprocess
from firebase import firebase
from firebase import jsonutil
from threading import Timer
class RepeatedTimer(object):
    def __init__(self, interval, function, *args, **kwargs):
        self._timer     = None
        self.interval   = interval
        self.function   = function
        self.args       = args
        self.kwargs     = kwargs
        self.is_running = False
        self.start()

    def _run(self):
        self.is_running = False
        self.start()
        self.function(*self.args, **self.kwargs)

    def start(self):
        if not self.is_running:
            self._timer = Timer(self.interval, self._run)
            self._timer.start()
            self.is_running = True

    def stop(self):
        self._timer.cancel()
        self.is_running = False

prevBuzzer = ""
prevWarningDistance = ""
ser = serial.Serial()

def indexOf(s1, s2):
    for i in range(0,len(s1)-len(s2)+1):
        if subString(s1,i,len(s2)) == s2:
            return i


    return -1

def subString(s, start, lenght = -1):
    temp = ""
    if lenght == -1:
        for i in range(start, len(s)):
            temp = temp + s[i]
    else:
        for i in range(start, start+lenght):
            temp = temp + s[i]
            
    return temp

def buzzerThread(fb):
    global prevBuzzer
    global ser
    buzzer = fb.get('/buzzer', None)
    if prevBuzzer != "":
        if prevBuzzer != buzzer:
            ser.write("b"+str(buzzer)+"\n")
            print("Buzzer: "+str(buzzer))
    else:
        ser.write("b"+str(buzzer)+"\n")
        print("Buzzer: "+str(buzzer))
    prevBuzzer = buzzer

def warningDistanceThread(fb):
    global prevWarningDistance
    global ser
    distance = fb.get('/distanceWarning', None)
    if prevWarningDistance != "":
        if prevWarningDistance != distance:
            ser.write(str(distance)+"\n")
            print(distance)
    else:
        ser.write(str(distance)+"\n")
        print("Warning Distance: "+str(distance))
    prevWarningDistance = distance

def relayThread(fb):
    global ser
    relay = fb.get('/relay', None)
    if relay == "0":
        ser.write("r0\n")
        print("Relay Off")
        result = firebase.put('/','relay', "1")
    
serPort = 0
try:
    #Open the serial connection
    while True:
        try:
            ser = serial.Serial('/dev/ttyACM'+str(serPort),9600)
            break
        except:
            serPort = serPort + 1
            serPort = serPort % 4
            continue
    firebase = firebase.FirebaseApplication('https://firstfirebase-b74f0.firebaseio.com', None)
    RepeatedTimer(1, buzzerThread, firebase)
    RepeatedTimer(1, warningDistanceThread, firebase)
    RepeatedTimer(1, relayThread, firebase)
    ##result = firebase.get('/0', 'ders')
    ##print(result)
    ##sfsadfsa = 'asdasd'
    while True:
            try:
                incomingData = ser.readline().strip()
                if(incomingData != ""):
                        print incomingData
                        if(incomingData[0]=='c'):
                                result = firebase.put('/','detect', '0')
                                result = firebase.put('/','distance', '0')
                                result = firebase.put('/','buzzer', "1")
                        elif(incomingData[0]=='1'):
                                result = firebase.put('/','buzzer', "1")
                        elif(incomingData[0]=='0'):
                                result = firebase.put('/','buzzer', "0")
                        elif(incomingData[0]=='s'):
                                if(incomingData[1]=='1'):
                                    result = firebase.put('/','line', "1")
                                elif(incomingData[1]=='0'):
                                    result = firebase.put('/','line', "0")
                        else:
                                if(incomingData[0]=='g'):
                                        data = subString(incomingData, 1)
                                        x = subString(data, 0, indexOf(data, " "))
                                        y = subString(data, indexOf(data, " ")+1)
                                        result = firebase.put('/','konum', {'X':x,'Y':y})
                                else:
                                        if(incomingData[0]=='p'):
                                                detect = "1"
                                        else:
                                                detect = "2"
                                        distance = subString(incomingData, 1)
                                        result = firebase.put('/','detect', detect)
                                        result = firebase.put('/','distance', distance)
                        currentTime = time.strftime('%H:%M')
                        currentDateToSend = time.strftime('%d/%m/%Y')
                        result = firebase.put('/DateTime','date', currentDateToSend)
                        result = firebase.put('/DateTime','time', currentTime)
                        ##result = firebase.put('/2', 'mesafe', {'date':currentDateToSend,'time':currentTime,'data':incomingData})
                        print result
                        incomingData = ""
            except KeyboardInterrupt:
                subprocess.call("killall python",shell=True)
            except:
                while True:
                    try:
                        ser = serial.Serial('/dev/ttyACM'+str(serPort),9600)
                        break
                    except:
                        serPort = serPort + 1
                        serPort = serPort % 4
                        continue
                
except KeyboardInterrupt:
    subprocess.call("killall python",shell=True)
