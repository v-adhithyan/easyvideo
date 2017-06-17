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
            config.remove_current_section()

    prompt = "pick"
    path = config.get_movie_folder()


    os_name = platform.system()
    item = ""

    if os_name == "Windows":


        answer = choice.Menu(os.listdir(path)).ask()
        os.chdir(path)
        while not os.path.isfile(answer):
            print "Chosen folder {}".format(answer)

            os.chdir(answer)
            answer = choice.Menu(os.listdir(path)).ask()
            #answer = inquirer.prompt(questions)

        item = answer
    else:
        import inquirer
        questions = [
            inquirer.List(prompt,
                          message="Pick something to watch",
                          choices=os.listdir(path)), ]
        os.chdir(path)
        answer = inquirer.prompt(questions)
        while not os.path.isfile(answer[prompt]):
            print "Chosen folder {}".format(answer[prompt])

            questions = [
                inquirer.List(prompt,
                          message="Pick something to watch",
                          choices=os.listdir(answer[prompt]))]

            os.chdir(answer[prompt])
            answer = inquirer.prompt(questions)
            item = answer[prompt]

    print "Picked {}".format(item)
    cwd = os.getcwd() + os.path.sep
    f = cwd + item
    config.write(CURRENT, MOVIE_NAME, item)
    config.write(CURRENT, FULL_PATH, f)

    if os_name == "Windows":
        shortcut = "{}.lnk".format(item)
        config.write(CURRENT, SHORTCUT, shortcut)
        print "Creating shortcut to {} in desktop".format(item)
        winshell.CreateShortcut(
            Path=os.path.join(winshell.desktop(), "{}.lnk".format(item)),
            Target=f,
            Description=item
        )
        os.startfile(item)
    elif os_name == "Darwin":
        retcode = subprocess.call("open {}".format(f), shell=False)
    else:
        print "Unsupported"