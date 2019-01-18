#!/bin/bash
git rm -r --cached .
git add .
git commit -am 'git cache cleared'
git push