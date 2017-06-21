import os
import pywinauto
import win32gui
import pyautogui
import ctypes
from easyvideo import *

def playback():
    handle = win32gui.GetForegroundWindow()
    app = pywinauto.application.Application().connect(handle=handle)
    window = app.Window_(handle=handle)
    window.TypeKeys("{SPACE}")

def list(current):
    path = config.get_android_folder()

    if path is None:
        path = config.get_movie_folder()

    path = os.path.join(path, current)
    config.set_android_folder(path)

    if os.path.isfile(path):
        return [], True

    return os.listdir(path), False

def volume_up():
    pyautogui.press("volumeup")

def volume_down():
    pyautogui.press("volumedown")

def mute_unmute():
    pyautogui.press("volumemute")

def go_fullscreen():
    pyautogui.press("f")

def lock():
    ctypes.windll.user32.LockWorkStation()

def shutdown():
    os.system("shutdown -s")
