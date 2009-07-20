i = 1;
j = 1
k = 1;


while (i <= 52)
  image = "nicubunu_Ornamental_deck_#{j}_of_#{k}.svg"
  print "(#{i},'#{image}',#{j},#{k}),"
  i += 1
  j += 1
p
  if (j == 14)
    k += 1
    j = 1
  end
end
