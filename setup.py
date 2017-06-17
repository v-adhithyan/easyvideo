from setuptools import setup

setup(
    name='easyvideo',
    version='1',
    install_requires=[
        'inquirer>=2.1.11',
        'blessings',
        'choice',
        'winshell'
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
