package cz.cuni.pedf.vovap.jirsak.geostezka.utils;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import cz.cuni.pedf.vovap.jirsak.geostezka.SettingsActivity;
import cz.cuni.pedf.vovap.jirsak.geostezka.WelcomeActivity;
import cz.cuni.pedf.vovap.jirsak.geostezka.tasks.ArTask;
import cz.cuni.pedf.vovap.jirsak.geostezka.tasks.CamTask;
import cz.cuni.pedf.vovap.jirsak.geostezka.tasks.DragDropTask;
import cz.cuni.pedf.vovap.jirsak.geostezka.R;
import cz.cuni.pedf.vovap.jirsak.geostezka.tasks.DrawTask;
import cz.cuni.pedf.vovap.jirsak.geostezka.tasks.GridTask;
import cz.cuni.pedf.vovap.jirsak.geostezka.tasks.QuizTask;
import cz.cuni.pedf.vovap.jirsak.geostezka.tasks.SwipeTask;

import static android.content.Context.MODE_PRIVATE;

public class Config {

    public static final int TYP_ULOHY_CAM = 1;
    public static final int TYP_ULOHY_DRAGDROP = 2;
    public static final int TYP_ULOHY_QUIZ = 3;
    public static final int TYP_ULOHY_AR = 4;
	public static final int TYP_ULOHY_GRID = 5;
    public static final int TYP_ULOHY_SWIPE = 6;
	public static final int TYP_ULOHY_DRAW = 7;

	public static final int TASK_STATUS_NOT_VISITED = 0;
	public static final int TASK_STATUS_OPENED = 1;
	public static final int TASK_STATUS_DONE = 2;

	public static final int TASK_ZULA_ID = 2;
	public static final int TASK_SLEPENEC_ID = 3;
	public static final int TASK_SLEPENEC2_ID = 4;

	private static Boolean DEBUG_MODE = null;

	public static final boolean poziceGeostezky(LatLng pozice) {
		/// polygon geostezky ///
		ArrayList<LatLng> points = new ArrayList<>();
		points.add(new LatLng(50.189739, 14.663800));
		points.add(new LatLng(50.190215, 14.663639));
		points.add(new LatLng(50.190303, 14.663961));
		points.add(new LatLng(50.189800, 14.664768));

		return isPointInPolygon(pozice, points);
	}
	private static boolean isPointInPolygon(LatLng tap, ArrayList<LatLng> vertices) {
		int intersectCount = 0;
		for(int j=0; j<vertices.size()-1; j++) {
			if( LineIntersect(tap, vertices.get(j), vertices.get(j+1)) ) {
				intersectCount++;
			}
		}
		return (intersectCount%2) == 1; // odd = inside, even = outside;
	}
	private static boolean LineIntersect(LatLng tap, LatLng vertA, LatLng vertB) {
		double aY = vertA.latitude;
		double bY = vertB.latitude;
		double aX = vertA.longitude;
		double bX = vertB.longitude;
		double pY = tap.latitude;
		double pX = tap.longitude;
		if ( (aY>pY && bY>pY) || (aY<pY && bY<pY) || (aX<pX && bX<pX) ) {
			return false; }
		double m = (aY-bY) / (aX-bX);
		double bee = (-aX) * m + aY;                // y = mx + b
		double x = (pY - bee) / m;
		return x > pX;
	}

	private static final Stanoviste[] SEZNAM_URL_VSECH_STANOVIST = {
			new Stanoviste(1, "Silicit", "http://polabskageostezka.cz/horniny/silicit-kremenec-buliznik/"),
			new Stanoviste(2, "Metadroba", "http://polabskageostezka.cz/horniny/metadroba/"),
			new Stanoviste(3, "Křemenec (Klecany)", "http://polabskageostezka.cz/horniny/silicit-kremenec/"),
			new Stanoviste(4, "Metabazalt (spilit)", "http://polabskageostezka.cz/horniny/metabazalt-spilit/"),
			new Stanoviste(5, "Konglomerát (slepenec)", "http://polabskageostezka.cz/horniny/konglomerat-slepenec/"),
			new Stanoviste(6, "Silicifikovaný pískovec (křemenec)", "http://polabskageostezka.cz/horniny/silicifikovany-piskovec-kremenec/"),
			new Stanoviste(7, "Silicifikovaný pískovec (křemenec)", "http://polabskageostezka.cz/horniny/silicifikovany-piskovec-kremenec1/"),
			new Stanoviste(8, "Prachovitý jílovec (břidlice)", "http://polabskageostezka.cz/horniny/prachovity-jilovec-bridlice/"),
			new Stanoviste(9, "Pararula", "http://polabskageostezka.cz/horniny/pararula/"),
			new Stanoviste(10, "Pyroxenit", "http://polabskageostezka.cz/horniny/pyroxenit/"),
			new Stanoviste(11, "Serpentinit (hadec)", "http://polabskageostezka.cz/horniny/serpentinit-hadec/"),
			new Stanoviste(12, "Amfibolit", "http://polabskageostezka.cz/horniny/amfibolit/"),
			new Stanoviste(13, "Migmatit", "http://polabskageostezka.cz/horniny/migmatit/"),
			new Stanoviste(14, "Fylit", "http://polabskageostezka.cz/horniny/fylit/"),
			new Stanoviste(15, "Liberecká žula (granit)", "http://polabskageostezka.cz/horniny/liberecka-zula-granit/"),
			new Stanoviste(16, "Granodiorit (žula-diorit)", "http://polabskageostezka.cz/horniny/granodiorit-zula-diorit/"),
			new Stanoviste(17, "Aplit", "http://polabskageostezka.cz/horniny/aplit/"),
			new Stanoviste(18, "Pegmatit", "http://polabskageostezka.cz/horniny/pegmatit/"),
			new Stanoviste(19, "Gabro", "http://polabskageostezka.cz/horniny/gabro/"),
			new Stanoviste(20, "Bulánecká brekcie", "http://polabskageostezka.cz/horniny/bulanecka-brekcie/"),
			new Stanoviste(21, "Melafyr (mandlovec)", "http://polabskageostezka.cz/horniny/melafyr-mandlovec/"),
			new Stanoviste(22, "Dadoxylon (kmen kordaitu)", "http://polabskageostezka.cz/horniny/dadoxylon-kmen-kordaitu/"),
			new Stanoviste(23, "Prachovitý pískovec", "http://polabskageostezka.cz/horniny/prachovity-piskovec/"),
			new Stanoviste(24, "Slepenec", "http://polabskageostezka.cz/horniny/slepenec/"),
			new Stanoviste(25, "Pískovec", "http://polabskageostezka.cz/horniny/piskovec/"),
			new Stanoviste(26, "Tempskya (nepravý kmen kapradin)", "http://polabskageostezka.cz/horniny/tempskya-nepravy-kmen-kapradin/"),
			new Stanoviste(27, "Písčito-prachovitý slínovec (opuka)", "http://polabskageostezka.cz/horniny/piscito-prachovity-slinovec-opuka/"),
			new Stanoviste(28, "Silkreta (křemenec - `sluňák`)", "http://polabskageostezka.cz/horniny/silkreta-kremenec-slunak/"),
			new Stanoviste(29, "Analcimit (bazalt - čedič)", "http://polabskageostezka.cz/horniny/analcimit-bazalt-cedic/"),
			new Stanoviste(30, "Bazalt (čedič)", "http://polabskageostezka.cz/horniny/bazalt-cedic/"),
			new Stanoviste(31, "Fonolit (znělec)", "http://polabskageostezka.cz/horniny/fonolit-znelec/"),
			new Stanoviste(32, "Štěrkopísek", "http://polabskageostezka.cz/horniny/sterkopisek/")
	};

	private static final Task[] SEZNAM_ULOH_INTRO = {
			// new Task(1,R.integer.TYP_ULOHY_CAM),
			// new Task(2,R.integer.TYP_ULOHY_DRAGDROP),
			// obecne task: id | pocet cilu | vysledky | nazev | zadani
			new CamTask(0,
                    "A",
					//new String[]{"0", "1"},
					new int[] {4,10,15,16,17,18,19,21,29,30,31},
					"Vyvřelé horniny",
					"Najdi všechny vyvřelé horniny v geoparku. Pro ověření načti QR kód na informační tabuli daného stanoviště.",
					"Gratulujeme!\n\nNašel jsi všechny vyvřelé horniny na geostezce.\nJdi na další úlohu.",
					"Špatně!\n\nToto není vyvřelá hornina.",
					new String[] {
							"Metabazalt/spilit je vulkanická - částečně metamorfovaná (vyvřelá výlevná - částečně přeměněná).",
							"Pyroxenit je magmatická (vyvřelá hlubinná)",
							"Liberecká žula (granit) je Magmatická (hlubinná vyvřelá).",
							"Žula (granit) je magmatická (hlubinná vyvřelá).",
							"Aplit je magmatická (vyvřelá hlubinná).",
							"Pegmatit je magmatická (vyvřelá) žilná.",
							"Gabro je magmatická (hlubinná vyvřelá).",
							"Melafyr (mandlovec) je vulkanická (vyvřelá výlevná).",
							"Analcimit (druh bazaltu - čediče) je vulkanická (vyvřelá výlevná).",
							"Bazalt (čedič) je vulkanická (vyvřelá výlevná).",
							"Fonolit (znělec) je vulkanická (vyvřelá výlevná)."
					},
					"http://0",
					1),
			new CamTask(1,
                    "B",
					//new String[]{"0", "1"},
					new int[] {15},
					"Hledání správné horniny",
					"Poznáš, z jaké horniny je tento nábrus? Najdi tuto horninu v geoparku a načti její QR kód.",
					"Gratulujeme!\n\nTímto jsi odemkl hlavní sadu úloh. Přejeme hodně štěstí.",
					"Špatně!\n\nToto není ta správná hornina. Podívej se pořádně na nábrus.",
					new String[] {},
					"http://1",
					-1)
	};


	/**
	 * vytvoreni pole se seznamem uloh
	 * ruzne konstruktory dle typu uloh
	 * obecne task: id | pocet cilu | vysledky | nazev | zadani | zpetna vazba
	 */
    private static final Task[] SEZNAM_ULOH = {
			// DD task: id | label | nazev | zadani | zpetna vazba OK | pozadi + zdrojove polozky | cilove polozky | afterclick | pozice cilu |QR | navaznost
			/// Zula
			new DragDropTask(2,
					SEZNAM_URL_VSECH_STANOVIST[14],
					"Přesuň správné minerály k vyznačeným místům na obrázku výbrusu žuly. Po správném přiřazení můžete poklepat na minerál pro zobrazení jeho krystalické mřížky.",
					"Výborně!\n\nNyní se podívej, jak vypadá mikroskopická struktura jednotlivých minerálů (poklepáním na minerál si můžeš změnit jeho " +
							"zobrazení).",
					R.layout.activity_task_drag_drop_zula,
					/// bck
					new int[]{R.drawable.granit_liberec},
					/// ikony pro pretazeni (prvni 3 dobre)
					new int[]{
							R.drawable.kremen_s, R.drawable.slida_s, R.drawable.zivec_s, R.drawable.sira_s, R.drawable.pyrit_s,
							R.drawable.halit_s, R.drawable.augit_s, R.drawable.beryl_s},
					/// cilova policka
					new int[]{R.drawable.zula_kremen_zoom, R.drawable.zula_biotit_zoom, R.drawable.zula_zivec_zoom},
					/// afterclick
					new int[]{R.drawable.zula_kremen_zoom, R.drawable.zula_biotit_zoom, R.drawable.zula_zivec_zoom},
					/// koordinatory cilovych policek (dle sirky 1080px)
					new Point[] {new Point(325,360), new Point(387,503), new Point(690,400)},
					new Point[]{},
					/// orientace terciku (jen pro zulu)
					new String[] {"left","right","right"},
					-1),
            // DragDropTask Slepenec -> retez na DrawTask
			new DragDropTask(TASK_SLEPENEC_ID,
					SEZNAM_URL_VSECH_STANOVIST[4],
					//"Slepenec - 1. část",
					"Zasaď valouny na správná místa.",
					"Výborně!\n\nTeď už jenom tmel.",
					R.layout.activity_task_drag_drop,
					new int[]{R.drawable.slepenec_cb_bezvalounu, R.drawable.slepenec_barva_final},
					new int[]{
							R.drawable.slep_valoun1, R.drawable.slep_valoun2, R.drawable.slep_valoun3, R.drawable.slep_valoun4,
							R.drawable.slep_valoun5, R.drawable.slep_valoun6, R.drawable.slep_valoun7},
					//new int[]{R.drawable.slep_valoun1, R.drawable.slep_valoun2, R.drawable.slep_valoun3},
					new int[]{},
					new int[]{},
					/// koordinatory stredu (k sirce 1080px)
					new Point[] {new Point(497,140),
							new Point(165,660),
							new Point(300,340),
							new Point(753,246),
							new Point(594,583),
							new Point(665,725),
							new Point(980,340)},
					/// sirka,vyska ciloveho policka
					new Point[]{new Point(117,135),
							new Point(255,200),
							new Point(75,80),
							new Point(150,120),
							new Point(75,85),
							new Point(103,123),
							new Point(188,222)},
					new String[] {},
					TASK_SLEPENEC2_ID,
					".1",
					" - 1. část"),
			/// Slepenec 2
			new DrawTask(TASK_SLEPENEC2_ID,
					SEZNAM_URL_VSECH_STANOVIST[4],
					//"5.2",
					//"Slepenec - 2. část",
					"Nyní vyplň tmel mezi valouny, aby se pěkně spojili.",
					R.layout.activity_task_draw,
					R.drawable.slepenec_barva_bezspar,
					R.drawable.slepenec_barva_final,
					"Skvělé!\n\nTeď máš kompletní slepenec. Můžeš ho porovnat s opravdovým vzorkem.",
					-1,
					".1",
					" - 1. část"),
			new ArTask(5,
                    "19",
					TYP_ULOHY_AR,
					"Gabro",
					"Namiř kamerou na obrázek na podstavci a prohlédněte si, jak vypadá gabro.",
					new String[] {"Gabro"},
					"Geostezka.xml",
					"Výborně!\n\nPomocí tažením nahoru/dolů a doprava/doleva můžeš kamenem otáčet a měnit jeho velikost.",
					"http://ARtest"),
            // DragDropTask Uhli
			new DragDropTask(6,
					SEZNAM_URL_VSECH_STANOVIST[24],
					"Přiřaď jednotlivé fáze rozkladu ke správné vrstvě horniny.....",
					"Výborně!",
					R.layout.activity_task_drag_drop,
					new int[]{R.drawable.slepenec_cb_bezvalounu, R.drawable.slepenec_barva_final},
					new int[]{
							R.drawable.slep_valoun1, R.drawable.slep_valoun2, R.drawable.slep_valoun3, R.drawable.slep_valoun4,
							R.drawable.slep_valoun5, R.drawable.slep_valoun6, R.drawable.slep_valoun7},
					new int[]{},
					new int[]{},
					/// koordinatory stredu (k sirce 1080px)
					new Point[] {new Point(497,140),
							new Point(165,660),
							new Point(300,340),
							new Point(753,246),
							new Point(594,583),
							new Point(665,725),
							new Point(980,340)},
					/// sirka,vyska ciloveho policka
					new Point[]{new Point(117,135),
							new Point(255,200),
							new Point(75,80),
							new Point(150,120),
							new Point(75,85),
							new Point(103,123),
							new Point(188,222)},
					new String[] {},
					-1),
			/*new DragDropTask(6,
					"4",
					"Uhlí",
					"Přesuň správné minerály k vyznačeným místům na obrázku výbrusu žuly. Po správném přiřazení můžete poklepat na minerál pro zobrazení jeho krystalické mřížky.",
					"Výborně! Nyní se podívej, jak vypadá mikroskopická struktura jednotlivých minerálů (poklepáním na minerál si můžeš změnit jeho zobrazení).",
					R.layout.activity_task_drag_drop_zula,
					R.drawable.granit_liberec,
					new int[]{
							R.drawable.kremen, R.drawable.slida_muskovit, R.drawable.zivec_ortoklas, R.drawable.biotit4, R.drawable.biotit4,
							R.drawable.kremen2, R.drawable.plagioklas1},
					new int[]{R.drawable.kremen, R.drawable.slida_muskovit, R.drawable.zivec_ortoklas},
					new int[]{R.drawable.afterclick, R.drawable.afterclick, R.drawable.afterclick},
					/// koordinatory na obr. sirky 1080px
					new Point[] {new Point(325,360), new Point(387,503), new Point(680,380)},
					new Point[] {},
					new String[] {"left","right","right"},
					//new Point[] {new Point(0,360), new Point(960,503), new Point(1920,380)},

					"http://4",
					-1),*/
            // ArTask Zkamenele drevo
			new ArTask(7,
					"22",
					TYP_ULOHY_AR,
					"Zkamenělé dřevo",
					"Namiř kamerou na obrázek na podstavci a prohlédněte si, jak vypadá gabro.",
					new String[] {"Drevo"},
					"Geostezka.xml",
					"Výborně! pomocí tažením nahoru/dolů a doprava/doleva můžeš kamenem otáčet a měnit jeho velikost.",
					"http://ARtest"),
			// Fylit
			new GridTask(8,
                    SEZNAM_URL_VSECH_STANOVIST[13],
					"Z přeložených obrázků vyber ten, který reprezentuje použití fylitu. Vždy je správně jen jeden.",
					"Výborně. Teď už víš, kde a jak se využívá či využíval fylit.",
					/// vždy je správně první prvek ze 4 (sady po 4)
					new int[] {R.drawable.fylit_tabulka_s, R.drawable.sira_s, R.drawable.sira_s, R.drawable.fylit_strecha_s,
							R.drawable.fylit_strecha_s, R.drawable.fylit_tabulka_s, R.drawable.fylit_tabulka_s, R.drawable.sira_s},
					new String[] {"Tabulka pro psaní ve škole.", "Výroba slídy", "Sirné doly", "Křemeny a křemeny",
							"Střecha kostela", "...", "...", "....",},
					new String[] {"Správně! Dříve se psalo ve školách křídou na fylitové tabulky.",
							"Ale ne, ...",
							"Ale ne, ...",
							"Ale ne, ...",
							"Správně! Z fylitu se vyráběly střešní tašky až do ...",
							"Ale ne, ...",
							"Ale ne, ...",
							"Ale ne, ...",},
					-1),
			/**
			* id | label | nazev | zadani | dilci zadani | odpovedi | URI | navaznost
			*/
			new QuizTask(9,
                    SEZNAM_URL_VSECH_STANOVIST[3],
					"Vyber správné odpovědi na jednotlivé otázky.",
                    new String[] {"Z ceho se sklada zula?", "Jaky je nejcasteji se vyskytujici se kamen?"},
					new QuizTaskItemConfig[] {
							new QuizTaskItemConfig("Biotit","Ano, Biotit je Metabazalt", true, 0),
							new QuizTaskItemConfig("Kremen","Špatně, křemen", false, 0),
							new QuizTaskItemConfig("Moznost neni k dispozici, ale ukazuje priklad dlouheho retezce v odpovedi","Špatně, není k dispozici",
									false,	0),

							new QuizTaskItemConfig("Slida","Ano, Slída je Biotit", true, 1),
							new QuizTaskItemConfig("Uvidíme 1","Špatně uvidíme 1", false, 1),
							new QuizTaskItemConfig("Uvidíme 2","Špatně uvidíme 2", false, 1),
							new QuizTaskItemConfig("Uvidíme 3","Špatně uvidíme 3", false, 1),
					},
                    -1),
			new QuizTask(10,
					SEZNAM_URL_VSECH_STANOVIST[12],
					"Vyber správné odpovědi na jednotlivé otázky.",
					new String[] {"Z ceho se sklada zula?", "Jaky je nejcasteji se vyskytujici se kamen?"},
					//new int[] {3, 4},
					new QuizTaskItemConfig[] {
							new QuizTaskItemConfig("Biotit","Ano, Biotit je Metabazalt", true, 0),
							new QuizTaskItemConfig("Kremen","Špatně, křemen", false, 0),
							new QuizTaskItemConfig("Moznost neni k dispozici, ale ukazuje priklad dlouheho retezce v odpovedi","Špatně, není k dispozici",
									false,	0),

							new QuizTaskItemConfig("Slida","Ano, Slída je Biotit", true, 1),
							new QuizTaskItemConfig("Uvidíme 1","Špatně uvidíme 1", false, 1),
							new QuizTaskItemConfig("Uvidíme 2","Špatně uvidíme 2", false, 1),
							new QuizTaskItemConfig("Uvidíme 3","Špatně uvidíme 3", false, 1),
					},
					-1),
            // ArTask Mandlovec
			new ArTask(11,
					"21",
					TYP_ULOHY_AR,
					"Mandlovec",
					"Namiř kamerou na obrázek na podstavci a prohlédněte si, jak vypadá gabro.",
					new String[] {"Achat"},
					"Geostezka.xml",
					"Výborně! pomocí tažením nahoru/dolů a doprava/doleva můžeš kamenem otáčet a měnit jeho velikost.",
					"http://ARtest"),
			new ArTask(12,
					"30",
					TYP_ULOHY_AR,
					"Čedič",
					"Namiř kamerou na obrázek na podstavci a prohlédněte si, jak vypadá gabro.",
					new String[] {"Cube"},
					"Geostezka.xml",
					"Výborně! pomocí tažením nahoru/dolů a doprava/doleva můžeš kamenem otáčet a měnit jeho velikost.",
					"http://ARtest"),
            // ArTask Cedic
            new SwipeTask(13,
					String.valueOf(SEZNAM_URL_VSECH_STANOVIST[31].getCislo()),
                    "Řeka",
                    "Poznáš podle uspořádání kamenů v korytě, jakým směrem tekla řeka?",
					new String[] {
							"Výborně!\nŘeka usměrnila valouny ve směru svého toku.",
							"Ale ne, takto řeka netekla."},
                    SEZNAM_URL_VSECH_STANOVIST[31].getUrl(),
                    -1)
			/*new CamTask(14,
					"CT",
					new String[]{"0", "1"},
					"Vyvřelé horniny",
					"Najdi QR 0 a 1",
					"Výborně! Jdi na další úlohu.",
					"http://0.cz",
					-1),
			new CamTask(15,
					"CT",
					new String[]{"0", "1"},
					"Vyvřelé horniny",
					"Najdi QR 0 a 1",
					"Výborně! Jdi na další úlohu.",
					"http://1.cz",
					-1),*/
    } ;


    /**
     * Metoda vraci objekt Uloha podle pozadovaneho id
     * @param hledaneid
     * @return
     */
    public static final Task vratUlohuPodleID(int hledaneid) {
        for(int i = 0; i < SEZNAM_ULOH.length; i++) {
            if(SEZNAM_ULOH[i].getId() == hledaneid) {
                return SEZNAM_ULOH[i];
            }
        }
        return null;
    }
    public static final Task vratUlohuPodleUri(String URI) {
        for(int i = 0; i < SEZNAM_ULOH.length; i++) {
            if(SEZNAM_ULOH[i].getUri().equals(URI)) {
                return SEZNAM_ULOH[i];
            }
        }
        return null;
    }
    public static final Stanoviste vratStanovistePodleUri(String url) {
		//Log.d("Geo CONFIG", "vratStanoviste URL: " + url);
    	for(int i = 0; i < SEZNAM_URL_VSECH_STANOVIST.length; i++) {
			//Log.d("Geo CONFIG", "vratStanoviste: " + i + " | " + SEZNAM_URL_VSECH_STANOVIST[i].getUrl());
    		if(SEZNAM_URL_VSECH_STANOVIST[i].getUrl().equals(url)) {
				return SEZNAM_URL_VSECH_STANOVIST[i];
			}
		}
		return null;
	}

	public static final Stanoviste vratStanovistePodleCisla(int cis) {
		//Log.d("Geo CONFIG", "vratStanoviste cislo: " + cis);
		for(int i = 0; i < SEZNAM_URL_VSECH_STANOVIST.length; i++) {
			if(SEZNAM_URL_VSECH_STANOVIST[i].getCislo() == cis) {
				return SEZNAM_URL_VSECH_STANOVIST[i];
			}
		}
		return null;
	}

    public static final int vratPocetUloh()
    {
        return SEZNAM_ULOH.length;
    }

    public static final Task vratIntroUlohuPodleID(int hledaneid) {
        for(int i = 0; i < SEZNAM_ULOH_INTRO.length; i++) {
            if(SEZNAM_ULOH_INTRO[i].getId() == hledaneid) {
                return SEZNAM_ULOH_INTRO[i];
            }
        }
        return null;
    }

    public static final int vratPocetUlohIntro() {
		return SEZNAM_ULOH_INTRO.length;
	}

	public static TextView getDebugTw(Context c) {
		TextView tw = new TextView(c);
		tw.setIncludeFontPadding(false);
		tw.setBackgroundColor(Color.DKGRAY);
		tw.setTextColor(Color.WHITE);
		tw.setVerticalScrollBarEnabled(true);
		tw.setMovementMethod(new ScrollingMovementMethod());
		tw.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
		//tw.setTag(0);
		return tw;
	}

	public static void showDebugMsg(final TextView tw, final String msg, Context c) {
		if(jeDebugOn(c)) {
			//if (tw.isEnabled()) {
				//tw.setTag((int) tw.getTag() + 1);
				if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
					//tw.setText(tw.getTag() + "| " + msg + "\n" + tw.getText());
					tw.setText(msg + "\n" + tw.getText());
				} else {
					Log.d("GEO Debug CONFIG", msg);
					Activity ac = (Activity) c;
					ac.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							//tw.setText(tw.getTag() + "| " + msg + "\n" + tw.getText());
							tw.setText(msg + "\n" + tw.getText());
						}
					});
				}
			//}
		}
	}

	public static boolean jeDebugOn(Context c) {
		if(DEBUG_MODE == null) {
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
			DEBUG_MODE = sp.getBoolean("pref_debug", false);
		}
		return DEBUG_MODE;
	}

	public static boolean isPositionCheckOn(Context c) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
		return !sp.getBoolean("pref_locationoff", false);
	}

	public static void nastavDebugMode(boolean stav, Context c) {
		DEBUG_MODE = stav;
	}

}
