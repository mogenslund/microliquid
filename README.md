# Micro Liquid
Micro Liquid is a very stripped down and in practice unuseable text editor written in Clojure.

The main purpose is to act as shortcut to understand it bigger sister [liquid](https://github.com/mogenslund/liquid)


Less than 300 lines
No IO
No special features or optimizations

1          Move backwards
2          Move forward
Backspace  Delete one char
Esc        Exit (Most special keys will do the same)
0          Switch between buffers

## Running
Micro Liquid runs in terminals on Linux and Mac

With Clojure installed execute the command

    clj -m microliquid.core

