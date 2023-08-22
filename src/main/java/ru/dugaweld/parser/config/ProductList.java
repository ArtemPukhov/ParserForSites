package ru.dugaweld.parser.config;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductList {

   public static Multiset<String> urls1 = ConcurrentHashMultiset.create();

}
