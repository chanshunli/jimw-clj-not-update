ewe(){
   file=$1
   eval "ee '(with-current-buffer \"scheme.clj\" (cider-interactive-eval \" (read-string-for-file (fn [code-list file-name] (map first code-list)) \\\"$file\\\") \"))'"
}

for sfile in ` find lib/scheme-jimw-code/ydiff -name "*.rkt" `; do ewe $sfile ; done

