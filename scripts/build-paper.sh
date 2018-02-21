#!/usr/bin/env bash
echo "checking for paper"

echo "downloading paper"
git clone --depth 1 https://github.com/PaperMC/Paper.git
cd Paper
./paper jar