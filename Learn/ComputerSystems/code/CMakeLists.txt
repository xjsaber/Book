cmake_minimum_required(VERSION 3.8)
project(code)

set(CMAKE_C_STANDARD 99)

set(SOURCE_FILES main.c process.c create_process.cpp nthread.c)
add_executable(code ${SOURCE_FILES})