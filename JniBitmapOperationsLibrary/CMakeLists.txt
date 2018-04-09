cmake_minimum_required(VERSION 3.4.1)

add_library(JniBitmapOperationsLibrary SHARED src/main/cpp/JniBitmapOperationsLibrary.cpp)
target_link_libraries(JniBitmapOperationsLibrary log android)