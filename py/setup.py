from setuptools import setup

setup(
    name='easyvideo',
    version='1',
    install_requires=[
        'choice>=0.1',
        'winshell>=0.6',
        'pywinauto>=0.6.2',
        'pywin32>=220',
        'pyautogui>=0.9.36',
    ],
    packages=['easyvideo'],
    entry_points={
        'console_scripts': ['easyvideo=easyvideo.cmd:main'],
    },
    url='https://github.com/v-adhithyan/easyvideo',
    license='MIT',
    author='Adhithyan V',
    author_email='v.adhithyan@gmail.com',
    description='Playing videos from desktop has never been easier.'
)
