#/bin/sh
file="build.properties"

if [ -f "$file" ]
then
  echo "$file found."

  while IFS='=' read -r key value
  do
    key=$(echo $key | tr '.' '_')
    eval "${key}='${value}'"
  done < "$file"
  
  echo "src.compile = " ${src_compile} 
else
  echo "$file not found"	
fi