import os
import pywinauto
import win32gui
from easyvideo import *

def playback():
    title = win32gui.GetWindowText(win32gui.GetForegroundWindow())
    app = pywinauto.application.Application().connect(title_re=title)
    window = app.Window_(title_re=title)
    window.TypeKeys("{SPACE}")
    #app = pywinauto.Application().connect(handle=handle)

def list(current):
    path = config.get_android_folder()

    if path is None:
        path = config.get_movie_folder()

    path = os.path.join(path, current)
    config.set_android_folder(path)

    if os.path.isfile(path):
        return [], True

    return os.listdir(path), False
