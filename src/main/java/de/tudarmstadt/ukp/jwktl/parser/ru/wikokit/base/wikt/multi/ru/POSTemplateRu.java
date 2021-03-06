/*******************************************************************************
 * Copyright 2008 Andrew Krizhanovsky <andrew.krizhanovsky at gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.tudarmstadt.ukp.jwktl.parser.ru.wikokit.base.wikt.multi.ru;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.tudarmstadt.ukp.jwktl.parser.ru.wikokit.base.wikipedia.util.StringUtil;
import de.tudarmstadt.ukp.jwktl.parser.ru.wikokit.base.wikt.constant.POS;
import de.tudarmstadt.ukp.jwktl.parser.ru.wikokit.base.wikt.constant.POSType;

/** Names of POS templates in Russian Wiktionary.
 * 
 * See http://ru.wiktionary.org/wiki/%D0%92%D0%B8%D0%BA%D0%B8%D1%81%D0%BB%D0%BE%D0%B2%D0%B0%D1%80%D1%8C:%D0%A7%D0%B0%D1%81%D1%82%D0%B8_%D1%80%D0%B5%D1%87%D0%B8
 *     http://ru.wiktionary.org/wiki/Викисловарь:Части речи
 *
 *     Категория:Шаблоны словоизменений
 */
public class POSTemplateRu extends POSType {

    /* POS name encountered in the Wiktionary, e.g.: {{acronym}} or Acronym, 
     * since there are ==={{acronym}}=== and ===Acronym===
     */
//    private final String name_in_text;
    
    /** POS name in Russian, e.g. "Акроним" for "acronym" */
    //private final String native_name; // todo ... HashMap type -> Russian POS name
    
    /** POS */
    private final POS type; 
    
    //private static Map<String, String>  text2name = new HashMap<String, String>();
    private static Map<String, POS> name_in_text2type = new HashMap<String, POS>();

    /** E.g. noun -> "сущ", "падежи", "фам". It is used in POS statistics. */
    private static Map<POS, Set<String>> type2name_in_text = new HashMap<POS, Set<String>>();

    private final static String[] NULL_STRING_ARRAY = new String[0];

    /** Initialization for POSTypeEn, POSTypeRu, etc. */
    private POSTemplateRu(String name_in_text, POS type) {
//        this.name_in_text   = name_in_text;
        this.type           = type;         // english.english;
        name_in_text2type.put(name_in_text, type); // english.english);

        {   // store (POS, +=name_in_text) -> type2name_in_text
            Set<String> templates = type2name_in_text.get(type);
            if(null == templates)
                templates = new HashSet<String>();

            templates.add(name_in_text);
            type2name_in_text.put(type, templates);
        }
    }
    
    public String getName() { return type.toString(); }
    
    /** Checks whether the part of speech with the abbreviation 'code' exists. */
    public static boolean has(String code) {
        return name_in_text2type.containsKey(code);
    }
    
    /** Check whether the given abbreviation 'code' contains a known
     *  part of speech tag. Return unknown if not. */
    public static POS isPOSIn(String code){
    	for (Map.Entry<String, POS> entry : name_in_text2type.entrySet()) {
    		if (code.contains(entry.getKey()))
    			return entry.getValue();
    	}
    	return POS.unknown;
    }
    
    /** Gets part of speech by its abbreviation or template */
    public static POS get(String code) {
        return name_in_text2type.get(code);
    }

    /** Gets (token separated) abbreviations or templates used (by parser) 
     * in order to recognize the "pos" part of speech.
     */
    public static String getTemplates(String token, POS pos) {

        Set<String> templates = type2name_in_text.get(pos);

        if(null == templates)
            return "";

        return StringUtil.join(", ", templates.toArray(NULL_STRING_ARRAY));
    }
    
    
    
    
    // The classical parts of speech are:
    
    // ===Морфологические и синтаксические свойства===
    // {{СущМужНеодуш1c(1)
    // {{СущЖенНеодуш8a
    // Существительное, ...
    // public static final POSLocal noun = new POSRu(, POS.noun);
    public static final POSType noun = new POSTemplateRu("сущ", POS.noun);
    public static final POSType noun_m_inanimate = new POSTemplateRu("сущмужнеодуш", POS.noun);// СущМужНеодуш-пол - noun
    public static final POSType noun_old= new POSTemplateRu("падежи",   POS.noun);// "существительное",
    public static final POSType noun_surname = new POSTemplateRu("фам",  POS.noun);// Фам - Surname (noun)

    

    // ===Морфологические и синтаксические свойства===
    // {{парадигма-рус
    // |шаблон=Гл11b/c
    //
    // {{Гл1a
    public static final POSType verb        = new POSTemplateRu("гл",       POS.verb);  // "глагол",
    public static final POSType verb_old_ru = new POSTemplateRu("глагол",   POS.verb);
    
    // {{adv-ru|
    // Наречие, неизменяемое.
    public static final POSType adverb_template     = new POSTemplateRu("adv",      POS.adverb);// "наречие", adv ru, adv-ru
    public static final POSType adverb_word         = new POSTemplateRu("наречие",  POS.adverb);// "наречие",
    public static final POSType adverb_word2        = new POSTemplateRu("нар",  POS.adverb); // "наречие", 
    
    // {{прил en|round|слоги=round}}
    public static final POSType adjective           = new POSTemplateRu("прил",     POS.adjective);// "прилагательное"
    public static final POSType adjective_old_en    = new POSTemplateRu("adjective",POS.adjective);
    // прил0 - used only once, skip
    // прил-сравн - works without this line, since "прил-сравн" starts from "прил-"... public static final POSType adjective_comparative_degree = new POSTemplateRu("прил-сравн",POS.adjective);

    // {{мест ru 6*b
    public static final POSType pronoun             = new POSTemplateRu("мест",        POS.pronoun);
    public static final POSType pronoun2      		= new POSTemplateRu("местоимения", POS.pronoun);
    public static final POSType pronoun_addon       = new POSTemplateRu("мс",          POS.pronoun);

    public static final POSType conjunction         = new POSTemplateRu("conj",     POS.conjunction);// союз
    public static final POSType conjunction2        = new POSTemplateRu("союз",     POS.conjunction);
    public static final POSType interjection        = new POSTemplateRu("interj",   POS.interjection);// междометие
    public static final POSType interjection2       = new POSTemplateRu("межд",     POS.interjection);
    public static final POSType preposition         = new POSTemplateRu("prep",     POS.preposition);// Предлог
    public static final POSType postposition        = new POSTemplateRu("послелог", POS.postposition);
    
    // Additional commonly used grammatical headers are:
    // proper_noun ?

    public static final POSType article             = new POSTemplateRu("art",      POS.article);// артикль
    public static final POSType article2            = new POSTemplateRu("article",  POS.article);

    public static final POSType prefix              = new POSTemplateRu("prefix",     POS.prefix);// приставка
    public static final POSType suffix              = new POSTemplateRu("suffix",     POS.suffix);// суффикс

    // phrase: there is special functions: WPOSRu.isPhrasePOS()
    // idiom - in phrase
    // prepositional_phrase - may be in phrase

    // debated POS level 3 headers
    public static final POSType numeral             = new POSTemplateRu("числ",     POS.numeral);// числительное

    // other descriptors that identify the usage of the entry, but which are not (strictly speaking) parts of speech:
    // acronym ?
    public static final POSType abbreviation        = new POSTemplateRu("abbrev",   POS.abbreviation);// Аббревиатура

    // other headers in use
    public static final POSType particle1 = new POSTemplateRu("part",     POS.particle);// частица, part ru, part-ru
    public static final POSType particle3 = new POSTemplateRu("particle", POS.particle);
    public static final POSType participle = new POSTemplateRu("прич", POS.participle);// Причастие
    public static final POSType predicative = new POSTemplateRu("predic", POS.predicative);// Именная часть составного сказуемого, предикатив


    // only in Russian Wiktionary (yet)
    public static final POSType verb_interjection = new POSTemplateRu("interj1", POS.verb_interjection);// interj1 - глагольно-междометное слово - verb-interjection word
    public static final POSType parenthesis = new POSTemplateRu("intro", POS.parenthesis);// Вводное слово
    public static final POSType prefix_of_compound = new POSTemplateRu("init", POS.prefix_of_compound);// первая часть сложных слов
    // ! "init" in ruwikt (Первая часть сложных слов) <> "initialism" in enwikt

    public static final POSType wordForm = new POSTemplateRu("форма", POS.unknown); // -- word form
    
    
}
