#!/bin/bash


admain=deployment.apps/dep-ad-main-server
screen=deployment.apps/dep-screen-server

a=$(kubectl apply -f ad-launch.yaml)
OLD_IFS="$IFS"
IFS=" "
arr=($a)
IFS="$OLD_IFS"
i=0
while  [[ i -lt "${#arr[@]}" ]];do
  str="${arr[$i]}"
  if [[ "$str" =~ .*"$admain".*  ]]; then
    ((i++));
    str="${arr[$i]}"
    if [[ "$str" =~  .*"unchanged".* ]]; then
      kubectl patch $admain -p   "{\"spec\":{\"template\":{\"metadata\":{\"annotations\":{\"date\":\"`date +'%s'`\"}}}}}"
    fi
  fi
  if [[ "$str" =~ .*"$screen".*  ]]; then
    ((i++));
    str="${arr[$i]}"
    if [[ "$str" =~  .*"unchanged".* ]]; then
      kubectl patch $screen -p   "{\"spec\":{\"template\":{\"metadata\":{\"annotations\":{\"date\":\"`date +'%s'`\"}}}}}"
    fi
  fi
    ((i++));
done

