# Micro Liquid
Micro Liquid is a very stripped down and in practice unuseable text editor written in Clojure.

There are no dependencies and code line count is below 300.

The main purpose is to act as shortcut to understand its bigger sister [liquid](https://github.com/mogenslund/liquid) by removing a lot of features and uptimization to reveal the simplicity underneath.

The secondary purpose is to act as a simple playground to develop and test features that migth be useful for [liquid](https://github.com/mogenslund/liquid).

## Only features

* Text can be inserted
* The cursor can be moved with keys "1" and "2"
* Switch between two buffers with "0"
* Delete a character with "backspace"
* Exit with anything transmitting an escape sequence, like "esc".

## Limitations
* No IO
* No linewrap og page management

## Running
Micro Liquid runs in terminals on Linux and Mac

With Clojure installed execute the command in the project folder:

    clj -m microliquid.core

