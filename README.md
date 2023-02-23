# Command Line Interpreter

## Purpose

An operating system interfaces with a user through a Command Line Interpreter `(CLI)`.

 A CLI is a software module capable of interpreting textual commands coming either from the user’s keyboard or from a script file. A CLI is often referred to as a shell.

## Description

In this assignment, you will write a Command Line Interpreter (CLI) for your operating system. Your CLI should prompt the user to enter the input through the keyboard. After a sequence of characters is entered followed by a return, the string is parsed and the indicated command(s) executed. The user is then again prompted for another command.
Your program implements some built-in commands; the list of required commands is listed below. Do not use exec to implement any of these commands. The exit command is also a special case: it should simply cause termination of your program.
