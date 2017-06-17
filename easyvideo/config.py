import os
import ConfigParser
import io
from easyvideo import MOVIE_FOLDER
from easyvideo import FOLDER
from easyvideo import CURRENT
from easyvideo import MOVIE_NAME
from easyvideo import SHORTCUT

home = os.path.expanduser("~") + os.path.sep
config_file = home + ".easyvideo.ini"

def config_file_exists():
    return os.path.isfile(config_file)

def write(section, key, value):
    if not config_file_exists():
        f = open(config_file, "w")
        f.close()

    with open(config_file, "a") as cfg:
        config = ConfigParser.ConfigParser()

        if not config.has_section(section=section):
            config.add_section(section)

        config.set(section, key, value)
        config.write(cfg)


def read(section, key):
    with open(config_file) as f:
        config_bytes = f.read()

        config = ConfigParser.RawConfigParser(allow_no_value=True)
        config.readfp(io.BytesIO(config_bytes))

        if section in config.sections():
            return config.get(section, key)

    return None

def remove_section(section):
    def read(section, key):
        with open(config_file) as f:
            config_bytes = f.read()

            config = ConfigParser.RawConfigParser(allow_no_value=True)
            config.readfp(io.BytesIO(config_bytes))

            if section in config.sections():
                config.remove_section(section=section)

def get_movie_folder():
    return read(MOVIE_FOLDER, FOLDER)

def get_current_movie():
    return read(CURRENT, MOVIE_NAME)

def get_current_shortcut():
    return read(CURRENT, SHORTCUT)

def remove_current_section():
    remove_section(CURRENT)