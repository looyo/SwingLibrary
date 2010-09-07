#!/usr/bin/env python

import os
import re
import sys
import subprocess
import glob

VERSION = '1.1.2-SNAPSHOT'

base = os.path.abspath(os.path.normpath(os.path.dirname(__file__)))

def call(cmd):
    print " ".join(cmd)
    return subprocess.call(cmd)

def build_projects():
    call(['mvn', '-Ddist.version=%s' % VERSION, '-f', 'core/pom.xml', 'clean', 'install', 'assembly:assembly'])
    call(['mvn', '-Ddist.version=%s' % VERSION, '-f', 'test-application/pom.xml', 'clean', 'install'])
    call(['mvn', '-Ddist.version=%s' % VERSION, '-f', 'test-keywords/pom.xml', 'clean', 'install', 'assembly:assembly'])
    call(['mvn', '-Ddist.version=%s' % VERSION, '-f', 'demo-application/pom.xml', 'clean', 'install', 'assembly:assembly']) 

def get_jar_with_dependencies_for(project):
    pattern = '%s/target/*-jar-with-dependencies.jar' % project
    paths = glob.glob(pattern)
    if paths:
        paths.sort()
        path = paths[-1]
    return os.path.abspath(os.path.abspath(path))

def init_dirs():
    call(['rm', '-r', 'target'])
    call(['mkdir', 'target'])

def copy_jars_to_target():
    call(['cp', os.path.join('core', 'target', 'swinglibrary-%s.jar' % VERSION), 'target'])
    call(['cp', os.path.join('core', 'target', 'swinglibrary-%s-jar-with-dependencies.jar' % VERSION), 'target'])

def exists(file_name):
    file = os.path.join(base, file_name)
    return os.path.exists(file)

def get_deps():
    os.environ['MAVEN_OPTS'] = '-DoutputAbsoluteArtifactFilename=true'
    mvn_output = sh('mvn -f core/pom.xml dependency:list').splitlines()
    jars = [re.sub('.*:((:?C:)?)', '\\1', file) for file in mvn_output if re.search('jar', file)]
    return jars

def sh(command):
    process = os.popen(command)
    output = process.read()
    process.close()
    return output

def add_dependencies_to_classpath():
    dependencies =  get_deps() + [os.path.join(base, 'core', 'target', 'classes')]
    for deb in dependencies:
        sys.path.append(deb)
    os.environ['CLASSPATH'] = os.pathsep.join(dependencies)

def create_doc():
    doc()
    assert_doc_ok()

def assert_doc_ok():
    doc_name = 'SwingLibrary-%s-doc.html' % VERSION
    docfile = open(os.path.join(base, 'doc', doc_name), 'r')
    for line in docfile.read().splitlines():
        if '*<unknown>' in line:
            raise "Errors in documentation: " + doc_name + " contains *<unknown>-tags."

def doc():
    add_dependencies_to_classpath()
    libdoc = os.path.join(base, 'lib', 'libdoc', 'libdoc.py')
    output = os.path.join(base, 'doc', 'SwingLibrary-%s-doc.html' % (VERSION))
    command = 'jython -Dpython.path=%s %s --output %s %s' % (os.path.join(base, 'lib', 'robotframework-2.5.2.jar', 'Lib'), libdoc, output, 'SwingLibrary')
    print command
    return os.system(command)

if __name__ == '__main__':
    init_dirs()
    build_projects()
    create_doc()
    copy_jars_to_target()

