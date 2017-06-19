import easyvideo
import subprocess
import os
import platform
from easyvideo import config
from easyvideo import MOVIE_FOLDER
from easyvideo import FOLDER
from easyvideo import CURRENT
from easyvideo import MOVIE_NAME
from easyvideo import FULL_PATH
from easyvideo import SHORTCUT
import winshell
import choice

def main():
    if not config.config_file_exists():
        path = raw_input("Enter the movie folder path:")
        config.write(MOVIE_FOLDER, FOLDER, path)

    current_movie = config.get_current_movie()
    if current_movie is not None:
        confirm = choice.Binary("Have you watched {}".format(current_movie), False).ask()

        if confirm:
            os.remove(config.get_current_shortcut())
            print "Removed shortcut from desktop .."

            delete = choice.Binary("Do you want to delete {} from the system?".format(current_movie), False).ask()
            if delete:
                os.remove(config.get_movie_path())
                print "Deleted {}".format(current_movie)
            config.remove_current_section()

    item = ""

    path = config.get_movie_folder()
    answer = choice.Menu(os.listdir(path)).ask()
    os.chdir(path)
    while not os.path.isfile(answer):
        print "Chosen folder: {}".format(answer)

        os.chdir(answer)
        answer = choice.Menu(os.listdir(path)).ask()

    print "Picked: {}".format(answer)
    cwd = os.getcwd() + os.path.sep
    f = cwd + item
    config.write(CURRENT, MOVIE_NAME, item)
    config.write(CURRENT, FULL_PATH, f)

    shortcut = "{}.lnk".format(item)
    config.write(CURRENT, SHORTCUT, os.path.join(winshell.desktop(), shortcut))
    print "Creating shortcut to {} in desktop".format(item)
    winshell.CreateShortcut(
        Path=os.path.join(winshell.desktop(), "{}.lnk".format(item)),
        Target=f,
        Description=item
    )
    os.startfile(item)