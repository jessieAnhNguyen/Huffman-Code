# Huffman-Code-UR

## This project uses the Huffman Coding to compress and decompress files. 

## File structure:
It contains 4 classes: BinaryIn, BinaryOut, Huffman, and HuffmanSubmit. The first 3 are source codes from the assignment. The HuffmanSubmit class implements the Huffman interface, and implements its two methods encode and decode.

HuffmanSubmit contains the sub-class Node, which represents a Node of the Huffman tree.

## Methods:
- encode: takes an inputFile, outputFile, and freqFile as parameters => encrypt the file using Huffman code.
- decode: takes an inputFile, outputFile, and freqFile as parameters => decrypt the file using Huffman code.
- helper methods such as buildHuffTree (build the Huffman tree), and createBinMap (map each character to its code)
- main method: test the encode and decode methods 

## Test files: 
- alice30.txt and ur.jpg
- Output files: alice30.enc, alicefreq.txt, alice30_dec.txt
              ur.enc, freq.txt, ur_dex.jpg


