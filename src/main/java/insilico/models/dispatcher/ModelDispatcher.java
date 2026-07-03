package insilico.models.dispatcher;

import insilico.apicalcardiotox.ApicalCardioTox;
import insilico.cardiotoxMultitask.CardioToxMultitask;
import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.exception.InitFailurePythonException;
import insilico.core.exception.PythonModelResourceNotFoundException;
import insilico.core.model.InsilicoModel;
import insilico.core.model.InsilicoModelConsensus;
import insilico.core.model.runner.iInsilicoModelRunnerMessenger;
import insilico.dilibayer.ismDiliBayer;
import insilico.dio1.ismDio1;
import insilico.mitochondrial_dysfunction.MitochondrialDysfunction;
import insilico.models.exception.ModelNotFoundException;
import insilico.ontox_assay.ismOntoxAssay;
import insilico.steroidogenesisedscreen.ismSteroidogenesisEDScreen;
import insilico.ttr.ismTTR;

import java.util.ArrayList;
import java.util.List;


public class ModelDispatcher {

    // Main sections to organize models
    public final static int SECTION_UNDEFINED = 0;
    public final static int SECTION_HUMAN = 1;
    public final static int SECTION_ECOTOX = 2;
    public final static int SECTION_FATE = 3;
    public final static int SECTION_PHYS = 4;
    public final static int SECTION_HUMAN_PBPK = 5;
    public final static int SECTION_ECO_PBPK = 6;

    public final static String[] SECTION_NAMES ={
            "N.A.",
            "Human Toxicity",
            "EcoToxicity",
            "Fate & Distribution",
            "Physical-Chemical properties",
            "Human PBPK",
            "Ecological PBPK",
    };


    // Complete list of model tags
    public final static String MUTA_CAESAR = "MUTA_CAESAR";
    public final static String MUTA_ISS = "MUTA_ISS";
    public final static String MUTA_SARPY = "MUTA_SARPY";
    public final static String MUTA_KNN = "MUTA_KNN";
    public final static String MUTA_AMINES = "MUTA_AMINES";
    public final static String MUTA_CONSENSUS = "MUTA_CONSENSUS";
    public final static String DEVTOX_CAESAR = "DEVTOX_CAESAR";
    public final static String DEVTOX_PG = "DEVTOX_PG";
    public final static String CARC_CAESAR = "CARC_CAESAR";
    public final static String CARC_ISS = "CARC_ISS";
    public final static String CARC_ISSCAN_CGX = "CARC_ISSCAN_CGX";
    public final static String CARC_ANTARES = "CARC_ANTARES";
    public final static String CARC_SFO_CLASS = "CARC_SFO_CLASS";
    public final static String CARC_SFO_REGR = "CARC_SFO_REGR";
    public final static String CARC_SFI_CLASS = "CARC_SFI_CLASS";
    public final static String CARC_SFI_REGR = "CARC_SFI_REGR";
    public final static String CARCINOGENICTY_MALE = "CARCINOGENICTY_MALE";
    public final static String CARCINOGENICTY_FEMALE = "CARCINOGENICTY_FEMALE";
    public final static String LD50_KNN = "LD50_KNN";
    public final static String SKIN_CAESAR = "SKIN_CAESAR";
    public final static String SKIN_IRFMN = "SKIN_IRFMN";
    public final static String SKIN_NCSTOX = "SKIN_NCSTOX";
    public final static String SKIN_SENSITIZATION_TOXTREE = "SKIN_SENSITIZATION_TOXTREE";
    public final static String SKIN_SENSITIZATION_CONCERT = "SKIN_SENSITIZATION_CONCERT";
    public final static String SKIN_SENSITIZATION_SARPY = "SKIN_SENSITIZATION_SARPY";
    public final static String SKIN_IRRITATION = "SKIN_IRRITATION";
    public final static String SKIN_IRRITATION_CORAL = "SKIN_IRRITATION_CORAL";
    public final static String SKIN_IRRITATION_SARPY = "SKIN_IRRITATION_SARPY";
    public final static String EYE_IRRITATION = "EYE_IRRITATION";
    public final static String EYE_IRRITATION_KNN = "EYE_IRRITATION_KNN";
    public final static String EYE_IRRITATION_SARPY = "EYE_IRRITATION_SARPY";
    public final static String CHROM_CORAL = "CHROM_CORAL";
    public final static String MNVITRO_VERMEER = "MNVITRO_VERMEER";
    public final static String MNVIVO_IRFMN = "MNVIVO_IRFMN";
    public final static String ESTROGEN_CERAPP = "ESTROGEN_CERAPP";
    public final static String RBA_IRFMN = "RBA_IRFMN";
    public final static String ANDROGEN_COMPARA = "ANDROGEN_COMPARA";
    public final static String TRALPHA_NRMEA = "TRALPHA_NRMEA";
    public final static String TRBETA_NRMEA = "TRBETA_NRMEA";
    public final static String GLUCO_RECEPTOR = "GLUCO_RECEPTOR";
    public final static String TPO_OBERON = "TPO_OBERON";
    public final static String ED_SCREEN = "ED_SCREEN";
    public final static String NOAEL_CONCERT_CORAL = "NOAEL_CONCERT_CORAL";
    public final static String NOAEL_CORAL = "NOAEL_CORAL";
    public final static String NOAEL_LIVER_CORAL = "NOAEL_LIVER_CORAL";
    public final static String LOAEL_CONCERT_CORAL = "LOAEL_CONCERT_CORAL";
    public final static String LOEL_LIVER_CORAL = "LOEL_LIVER_CORAL";
    public final static String CRAMER_TOXTREE = "CRAMER_TOXTREE";
    public final static String HEPA_IRFMN = "HEPA_IRFMN";
    public final static String BCF_CAESAR = "BCF_CAESAR";
    public final static String BCF_MEYLAN = "BCF_MEYLAN";
    public final static String BCF_ARNOTGOBAS = "BCF_ARNOTGOBAS";
    public final static String BCF_KNN = "BCF_KNN";
    public final static String FISH_LC50 = "FISH_LC50";
    public final static String FISH_NIC = "FISH_NIC";
    public final static String FISH_KNN = "FISH_KNN";
    public final static String FISH_IRFMN = "FISH_IRFMN";
    public final static String FISH_COMBASE = "FISH_COMBASE";
    public final static String FATHEAD_EPA = "FATHEAD_EPA";
    public final static String FATHEAD_KNN = "FATHEAD_KNN";
    public final static String DAPHNIA_EC50 = "DAPHNIA_EC50";
    public final static String DAPHNIA_EPA = "DAPHNIA_EPA";
    public final static String DAPHNIA_DEMETRA = "DAPHNIA_DEMETRA";
    public final static String DAPHNIA_COMBASE = "DAPHNIA_COMBASE";
    public final static String GUPPY_KNN = "GUPPY_KNN";
    public final static String ALGAE_EC50 = "ALGAE_EC50";
    public final static String ALGAE_COMBASECLASS = "ALGAE_COMBASECLASS";
    public final static String ALGAE_COMBASEEC50 = "ALGAE_COMBASEEC50";
    public final static String FISH_NOEC = "FISH_NOEC";
    public final static String DAPHNIA_NOEC = "DAPHNIA_NOEC";
    public final static String ALGAE_NOEC = "ALGAE_NOEC";
    public final static String VERHAAR_TOXTREE = "VERHAAR_TOXTREE";
    public final static String MOA_EPA = "MOA_EPA";
    public final static String MOA_IRFMN = "MOA_IRFMN";
    public final static String BEE_KNN = "BEE_KNN";
    public final static String EW_TOXICITY = "EW_TOXICITY";
    public final static String SLUDGE_COMBASECLASS = "SLUDGE_COMBASECLASS";
    public final static String SLUDGE_COMBASEEC50 = "SLUDGE_COMBASEEC50";
    public final static String ZEBRAFISH_CORAL = "ZEBRAFISH_CORAL";
    public final static String READYBIO_IRFMN = "READYBIO_IRFMN";
    public final static String PERS_SED = "PERS_SED";
    public final static String PERS_SED_QUANT = "PERS_SED_QUANT";
    public final static String PERS_SOIL = "PERS_SOIL";
    public final static String PERS_SOIL_QUANT = "PERS_SOIL_QUANT";
    public final static String PERS_WAT = "PERS_WAT";
    public final static String PERS_WATER_QUANT = "PERS_WATER_QUANT";
    public final static String PERS_AIR_CORAL = "PERS_AIR_CORAL";
    public final static String LOGP_MEYLAN = "LOGP_MEYLAN";
    public final static String LOGP_MLOGP = "LOGP_MLOGP";
    public final static String LOGP_ALOGP = "LOGP_ALOGP";
    public final static String WS_IRFMN = "WS_IRFMN";
    public final static String VAPOUR_PRESSURE = "VAPOUR_PRESSURE";
    public final static String MELTING_POINT = "MELTING_POINT";
    public final static String MELTING_POINT_KNN = "MELTING_POINT_KNN";
    public final static String HYDROLYSIS_CORAL = "HYDROLYSIS_CORAL";
    public final static String HENRY_OPERA = "HENRY_OPERA";
    public final static String KOA_OPERA = "KOA_OPERA";
    public final static String KOC_OPERA = "KOC_OPERA";
    public final static String PPB_LOGK = "PPB_LOGK";
    public final static String PPB_CORAL = "PPB_CORAL";
    public final static String AROM_IRMFN = "AROM_IRMFN";
    public final static String AROM_TOX21 = "AROM_TOX21";
    public final static String PGP_NIC = "PGP_NIC";
    public final static String HEPA_PXRUP = "HEPA_PXRUP";
    public final static String HEPA_PPARGUP = "HEPA_PPARGUP";
    public final static String HEPA_PPARAUP = "HEPA_PPARAUP";
    public final static String HEPA_NRF2 = "HEPA_NRF2";
    public final static String SKINPERM_POTTS = "SKINPERM_POTTS";
    public final static String SKINPERM_TENBERGE = "SKINPERM_TENBERGE";
    public final static String TISSUEBLOOD_INERIS = "TISSUEBLOOD_INERIS";
    public final static String TOTALHL_QSARINS = "TOTALHL_QSARINS";
    public final static String KM_ARNOT = "KM_ARNOT";
    public final static String DILI_BAYER = "DILI_ONTOX";
    public final static String MITOCHONDRIAL_DYSFUNCTION = "MITO_DYSF";
    public final static String APICAL_CARDIO_TOX = "API_CARDIO";
    public final static String CARDIO_TOX_MULTITASK = "CARDIO_MULTITASK";
    public final static String ACE_ONTOX = "ACE_ONTOX";
    public final static String NMDA_ONTOX = "NMDA_ONTOX";
    public final static String PXR_ONTOX = "PXR_ONTOX";
    public final static String OAT1_ONTOX = "OAT1_ONTOX";
    public final static String COX1_ONTOX = "COX1_ONTOX";
    public final static String AT1R_ONTOX = "AT1R_ONTOX";
    public final static String TTR_ONTOX = "TTR_ONTOX";
    public final static String BSEP_ONTOX = "BSEP_ONTOX";
    public final static String ACHE_ONTOX = "ACHE_ONTOX";
    public final static String FGFR1_ONTOX = "FGFR1_ONTOX";
    public final static String FGFR2_ONTOX = "FGFR2_ONTOX";
    public final static String FGFR3_ONTOX = "FGFR3_ONTOX";
    public final static String FGFR4_ONTOX = "FGFR4_ONTOX";
    public final static String BMP_ONTOX = "BMP_ONTOX";
    public final static String AHR_ONTOX = "AHR_ONTOX";
    public final static String WNT_ONTOX = "WNT_ONTOX";
    public final static String PPARA_ONTOX = "PPARA_ONTOX";
    public final static String PPARD_ONTOX = "PPARD_ONTOX";
    public final static String PPARG_ONTOX = "PPARG_ONTOX";
    public final static String HDEAC_ONTOX = "HDEAC_ONTOX";
    public final static String THRB_ONTOX = "THRB_ONTOX";
    public final static String CYP26_ONTOX = "CYP26_ONTOX";
    public final static String THRA_ONTOX = "THRA_ONTOX";
    public final static String VGSC_ONTOX = "VGSC_ONTOX";
    public final static String GR_ONTOX = "GR_ONTOX";
    public final static String DIO1_EDSCREEN = "DIO1_EDSCREEN";
    public final static String STERO_EDSCREEN = "STERO_EDSCREEN";
    public final static String TTR_EDSCREEN = "TTR_EDSCREEN";



    // supporting class to organize endpoints
    public class VegaEndpoint {
        public String Name;
        public int Section;
        public ArrayList<String> Models;
        public ArrayList<String> ModelsConsensus;

        public VegaEndpoint(String name, int section) {
            Name = name;
            Section = section;
            Models = new ArrayList<>();
            ModelsConsensus = new ArrayList<>();
        }

        public void AddModel(String model) {
            this.Models.add(model);
        }

        public void AddModelConsensus(String model) {
            this.ModelsConsensus.add(model);
        }
    }

    public static class VegaEndpointWithClass {
        public String Name;
        public int Section;
        public ArrayList<InsilicoModel> Models;

        public VegaEndpointWithClass(String name, int Section) {
            Name = name;
            this.Section = Section;
            Models = new ArrayList<>();
        }

        public void addModel(InsilicoModel model) {
            this.Models.add(model);
        }
    }

    public void PrintModelsList() throws Exception {
        ArrayList<String> res = new ArrayList<>();

        ArrayList<VegaEndpoint> Endpoints = GetOrganizedModels();
        int idx = 1;
        for (VegaEndpoint EP : Endpoints) {
            for (String modelTag : EP.Models) {
                InsilicoModel model = GetModelFromTag(modelTag);
                res.add(idx++ + "\t" + SECTION_NAMES[EP.Section] + "\t" + modelTag + "\t"
                        + model.getInfo().getName() + "\t" + model.getInfo().getVersion() );
            }
        }

        System.out.println("No.\tSection\tTag\tModel\tVersion");
        for (String s : res)
            System.out.println(s);
    }

    public List<InsilicoModel> getCompleteModelList() throws ModelNotFoundException, InitFailureException, GenericFailureException {
        ArrayList<InsilicoModel> models = new ArrayList<>();
        ArrayList<VegaEndpoint> Endpoints = GetOrganizedModels();

        int idx = 1;
        for (VegaEndpoint EP : Endpoints) {
            for (String modelTag : EP.Models) {
                InsilicoModel model = GetModelFromTag(modelTag, null, true);
                models.add(model);
            }
        }
        return models;
    }

    /**
     *
     * @return
     */
    public ArrayList<VegaEndpoint> GetOrganizedModels() {
        ArrayList<VegaEndpoint> Endpoints = new ArrayList<>();
        VegaEndpoint ep;

        ep = new VegaEndpoint("Mutagenicity (Ames test)", SECTION_HUMAN);
        ep.AddModel(MUTA_CAESAR);
        ep.AddModel(MUTA_ISS);
        ep.AddModel(MUTA_SARPY);
        ep.AddModel(MUTA_KNN);
        ep.AddModel(MUTA_AMINES);
        ep.AddModelConsensus(MUTA_CONSENSUS);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Developmental toxicity", SECTION_HUMAN);
        ep.AddModel(DEVTOX_CAESAR);
        ep.AddModel(DEVTOX_PG);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Carcinogenicity", SECTION_HUMAN);
        ep.AddModel(CARC_CAESAR);
        ep.AddModel(CARC_ISS);
        ep.AddModel(CARC_ISSCAN_CGX);
        ep.AddModel(CARC_ANTARES);
        ep.AddModel(CARC_SFO_CLASS);
        ep.AddModel(CARC_SFO_REGR);
        ep.AddModel(CARC_SFI_CLASS);
        ep.AddModel(CARC_SFI_REGR);
        ep.AddModel(CARCINOGENICTY_MALE); //todo cambiare tag
        ep.AddModel(CARCINOGENICTY_FEMALE); //todo cambiare tag
        Endpoints.add(ep);

        ep = new VegaEndpoint("Acute Toxicity (LD50)", SECTION_HUMAN);
        ep.AddModel(LD50_KNN);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Skin Sensitization", SECTION_HUMAN);
        ep.AddModel(SKIN_CAESAR);
        ep.AddModel(SKIN_IRFMN);
        ep.AddModel(SKIN_NCSTOX);
        ep.AddModel(SKIN_SENSITIZATION_TOXTREE);
        ep.AddModel(SKIN_SENSITIZATION_CONCERT);
        ep.AddModel(SKIN_SENSITIZATION_SARPY);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Skin Irritation", SECTION_HUMAN);
        ep.AddModel(SKIN_IRRITATION);
        ep.AddModel(SKIN_IRRITATION_CORAL);
        ep.AddModel(SKIN_IRRITATION_SARPY);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Eye Irritation", SECTION_HUMAN);
        ep.AddModel(EYE_IRRITATION);
        ep.AddModel(EYE_IRRITATION_KNN);
        ep.AddModel(EYE_IRRITATION_SARPY);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Chromosomal aberration", SECTION_HUMAN);
        ep.AddModel(CHROM_CORAL);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Micronucleus assay", SECTION_HUMAN);
        ep.AddModel(MNVITRO_VERMEER);
        ep.AddModel(MNVIVO_IRFMN);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Estrogen receptor effect", SECTION_HUMAN);
        ep.AddModel(ESTROGEN_CERAPP);
        ep.AddModel(RBA_IRFMN);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Androgen receptor effect", SECTION_HUMAN);
        ep.AddModel(ANDROGEN_COMPARA);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Thyroid receptor effect", SECTION_HUMAN);
        ep.AddModel(TRALPHA_NRMEA);
        ep.AddModel(TRBETA_NRMEA);
        ep.AddModel(TTR_EDSCREEN);
        ep.AddModel(DIO1_EDSCREEN);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Glucocorticoid Receptor effect", SECTION_HUMAN);
        ep.AddModel(GLUCO_RECEPTOR);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Thyroperoxidase Inhibitory activity", SECTION_HUMAN);
        ep.AddModel(TPO_OBERON);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Steroidogenesis activity", SECTION_HUMAN);
        ep.AddModel(STERO_EDSCREEN);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Endocrine Disruptor activity", SECTION_HUMAN);
        ep.AddModel(ED_SCREEN);
        Endpoints.add(ep);

        ep = new VegaEndpoint("NOAEL", SECTION_HUMAN);
        ep.AddModel(NOAEL_CONCERT_CORAL);
        ep.AddModel(NOAEL_CORAL);
        ep.AddModel(NOAEL_LIVER_CORAL);
        Endpoints.add(ep);

        ep = new VegaEndpoint("LOAEL", SECTION_HUMAN);
        ep.AddModel(LOAEL_CONCERT_CORAL);
        ep.AddModel(LOEL_LIVER_CORAL); //todo sistemare nome
        Endpoints.add(ep);

        ep = new VegaEndpoint("Cramer classification", SECTION_HUMAN);
        ep.AddModel(CRAMER_TOXTREE);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Hepatotoxicity", SECTION_HUMAN);
        ep.AddModel(HEPA_IRFMN);
        ep.AddModel(DILI_BAYER);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Cardiotoxicity", SECTION_HUMAN);
        ep.AddModel(APICAL_CARDIO_TOX);
        ep.AddModel(CARDIO_TOX_MULTITASK);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Mitochondrial dysfunction", SECTION_HUMAN);
        ep.AddModel(MITOCHONDRIAL_DYSFUNCTION);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Kidney failure (KF)", SECTION_HUMAN);
        ep.AddModel(ACE_ONTOX);
        ep.AddModel(OAT1_ONTOX);
        ep.AddModel(COX1_ONTOX);
        ep.AddModel(AT1R_ONTOX);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Cholestasis  (CHO)", SECTION_HUMAN);
        ep.AddModel(BSEP_ONTOX);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Neural tube closure (NTD)", SECTION_HUMAN);
        ep.AddModel(HDEAC_ONTOX);
        ep.AddModel(CYP26_ONTOX);
        ep.AddModel(BMP_ONTOX);
        ep.AddModel(WNT_ONTOX);
        ep.AddModel(FGFR1_ONTOX);
        ep.AddModel(FGFR2_ONTOX);
        ep.AddModel(FGFR3_ONTOX);
        ep.AddModel(FGFR4_ONTOX);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Cognitive function defects (CFD)", SECTION_HUMAN);
        ep.AddModel(TTR_ONTOX);
        ep.AddModel(ACHE_ONTOX);
        ep.AddModel(THRB_ONTOX);
        ep.AddModel(THRA_ONTOX);
        ep.AddModel(VGSC_ONTOX);
        ep.AddModel(NMDA_ONTOX);
        ep.AddModel(PPARD_ONTOX);
        ep.AddModel(GR_ONTOX);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Liver steatosis (STE)", SECTION_HUMAN);
        ep.AddModel(PXR_ONTOX);
        ep.AddModel(AHR_ONTOX);
        ep.AddModel(PPARA_ONTOX);
        ep.AddModel(PPARG_ONTOX);
        Endpoints.add(ep);

        // Ecotox

        ep = new VegaEndpoint("BCF", SECTION_ECOTOX);
        ep.AddModel(BCF_CAESAR);
        ep.AddModel(BCF_MEYLAN);
        ep.AddModel(BCF_ARNOTGOBAS);
        ep.AddModel(BCF_KNN);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Aquatic Acute Toxicity", SECTION_ECOTOX);
        ep.AddModel(FISH_LC50);
        ep.AddModel(FISH_NIC);
        ep.AddModel(FISH_KNN);
        ep.AddModel(FISH_IRFMN);
        ep.AddModel(FISH_COMBASE);
        ep.AddModel(FATHEAD_EPA);
        ep.AddModel(FATHEAD_KNN);
        ep.AddModel(DAPHNIA_EC50);
        ep.AddModel(DAPHNIA_EPA);
        ep.AddModel(DAPHNIA_DEMETRA);
        ep.AddModel(DAPHNIA_COMBASE);
        ep.AddModel(GUPPY_KNN);
        ep.AddModel(ALGAE_EC50);
        ep.AddModel(ALGAE_COMBASECLASS);
        ep.AddModel(ALGAE_COMBASEEC50);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Aquatic Chronic Toxicity", SECTION_ECOTOX);
        ep.AddModel(FISH_NOEC);
        ep.AddModel(DAPHNIA_NOEC);
        ep.AddModel(ALGAE_NOEC);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Mode of Action", SECTION_ECOTOX);
        ep.AddModel(VERHAAR_TOXTREE);
        ep.AddModel(MOA_EPA);
        ep.AddModel(MOA_IRFMN);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Terrestrial Acute Toxicity", SECTION_ECOTOX);
        ep.AddModel(BEE_KNN);
        ep.AddModel(EW_TOXICITY);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Sludge Toxicity", SECTION_ECOTOX);
        ep.AddModel(SLUDGE_COMBASECLASS);
        ep.AddModel(SLUDGE_COMBASEEC50);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Zebrafish embryo activity", SECTION_ECOTOX);
        ep.AddModel(ZEBRAFISH_CORAL);
        Endpoints.add(ep);


        // Fate and Distribution

        ep = new VegaEndpoint("Ready biodegradability", SECTION_FATE);
        ep.AddModel(READYBIO_IRFMN);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Persistence (sediment)", SECTION_FATE);
        ep.AddModel(PERS_SED);
        ep.AddModel(PERS_SED_QUANT);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Persistence (soil)", SECTION_FATE);
        ep.AddModel(PERS_SOIL);
        ep.AddModel(PERS_SOIL_QUANT);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Persistence (water)", SECTION_FATE);
        ep.AddModel(PERS_WAT);
        ep.AddModel(PERS_WATER_QUANT);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Persistence (air)", SECTION_FATE);
        ep.AddModel(PERS_AIR_CORAL);
        Endpoints.add(ep);


        // Physical Chemical properties

        ep = new VegaEndpoint("Octanol/Water partition coefficient (logP)", SECTION_PHYS);
        ep.AddModel(LOGP_MEYLAN);
        ep.AddModel(LOGP_MLOGP);
        ep.AddModel(LOGP_ALOGP);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Water solubility", SECTION_PHYS);
        ep.AddModel(WS_IRFMN);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Vapour pressure", SECTION_PHYS);
        ep.AddModel(VAPOUR_PRESSURE);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Melting point", SECTION_PHYS);
        ep.AddModel(MELTING_POINT);
        ep.AddModel(MELTING_POINT_KNN);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Hydrolysis", SECTION_PHYS);
        ep.AddModel(HYDROLYSIS_CORAL);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Henry's law constant", SECTION_PHYS);
        ep.AddModel(HENRY_OPERA);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Octanol/air partition coefficient (KOA)", SECTION_PHYS);
        ep.AddModel(KOA_OPERA);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Soil adsorption coefficient of organic compounds (KOC)", SECTION_PHYS);
        ep.AddModel(KOC_OPERA);
        Endpoints.add(ep);


        // Human PBPK

        ep = new VegaEndpoint("Plasma Protein Binding", SECTION_HUMAN_PBPK);
        ep.AddModel(PPB_LOGK);
        ep.AddModel(PPB_CORAL);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Aromatase activity", SECTION_HUMAN_PBPK);
        ep.AddModel(AROM_IRMFN);
        ep.AddModel(AROM_TOX21);
        Endpoints.add(ep);

        ep = new VegaEndpoint("P-Glycoprotein activity", SECTION_HUMAN_PBPK);
        ep.AddModel(PGP_NIC);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Hepatic Steatosis MIE", SECTION_HUMAN_PBPK);
        ep.AddModel(HEPA_PXRUP);
        ep.AddModel(HEPA_PPARGUP);
        ep.AddModel(HEPA_PPARAUP);
        ep.AddModel(HEPA_NRF2);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Skin permeation (LogKp)", SECTION_HUMAN_PBPK);
        ep.AddModel(SKINPERM_POTTS);
        ep.AddModel(SKINPERM_TENBERGE);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Adipose tissue-blood partition", SECTION_HUMAN_PBPK);
        ep.AddModel(TISSUEBLOOD_INERIS);
        Endpoints.add(ep);

        ep = new VegaEndpoint("Body elimination half-life", SECTION_HUMAN_PBPK);
        ep.AddModel(TOTALHL_QSARINS);
        Endpoints.add(ep);


        // Eco PBPK

        ep = new VegaEndpoint("kM/Half Life", SECTION_ECO_PBPK);
        ep.AddModel(KM_ARNOT);
        Endpoints.add(ep);


        return Endpoints;
    }

    public ArrayList<VegaEndpointWithClass> GetOrganizedModelList() throws InitFailureException, GenericFailureException, PythonModelResourceNotFoundException {
        ArrayList<VegaEndpointWithClass> endpointsList = new ArrayList<>();
        VegaEndpointWithClass ep;

        ep = new VegaEndpointWithClass("Mutagenicity (Ames test)", SECTION_HUMAN);
        ep.addModel(new insilico.mutagenicity_caesar.ismMutagenicityCaesar());
        ep.addModel(new insilico.mutagenicity_bb.ismMutagenicityBB());
        ep.addModel(new insilico.mutagenicity_sarpy.ismMutagenicitySarpy());
        ep.addModel(new insilico.mutagenicity_knn.ismMutagenicityKnn());
        ep.addModel(new insilico.mutagenicity_amines.ismMutagenicityAmines());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Developmental toxicity", SECTION_HUMAN);
        ep.addModel(new insilico.devtox_caesar.ismDevtoxCaesar());
        ep.addModel(new insilico.devtox_pg.ismDevToxPG());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Carcinogenicity", SECTION_HUMAN);
        ep.addModel(new insilico.carcinogenicity_caesar.ismCarcinogenicityCaesar());
        ep.addModel(new insilico.carcinogenicity_bb.ismCarcinogenicityBB());
        ep.addModel(new insilico.carcinogenicity_isscancgx.ismCarcinogenicityIsscanCgx());
        ep.addModel(new insilico.carcinogenicity_antares.ismCarcinogenicityAntares());
        ep.addModel(new insilico.carcinogenicity_sfoclassification.ismCarcinogenicitySFOClassification());
        ep.addModel(new insilico.carcinogenicity_sforegression.ismCarcinogenicitySFORegression());
        ep.addModel(new insilico.carcinogenicity_sfi_classification.ismCarcinogenicitySFIClassification());
        ep.addModel(new insilico.carcinogenicity_sfiregression.ismCarcinogenicitySFIRegression());
        ep.addModel(new insilico.carcinogenicity_rat_male.ismCarcinogenicityRatMale());
        ep.addModel(new insilico.carcinogenicity_rat_female.ismCarcinogenicityRatFemale());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Acute Toxicity (LD50)", SECTION_HUMAN);
        ep.addModel(new insilico.ld50.ismLD50());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Skin Sensitization", SECTION_HUMAN);
        ep.addModel(new insilico.skin_caesar.ismSkinCaesar());
        ep.addModel(new insilico.skin_irfmn.ismSkinIRFMN());
        ep.addModel(new insilico.skin_cosmetics.ismSkinCosmetics());
        ep.addModel(new insilico.skin_sensitization_toxtree.ismSkinSensitizationToxTree());
        ep.addModel(new insilico.skin_sensitization_concert.ismSkinSensitizationConcert());
        ep.addModel(new insilico.skin_sensitization_sarpy.ismSkinSensitizationSarpy());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Skin Irritation", SECTION_HUMAN);
        ep.addModel(new insilico.skin_irritation.ismSkinIrritation());
        ep.addModel(new insilico.skin_irritation_coral.ismSkinIrritationCoral());
        ep.addModel(new insilico.skin_irritation_sarpy.ismSkinIrritationSarpy());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Eye Irritation", SECTION_HUMAN);
        ep.addModel(new insilico.eye_irritation.ismEyeIrritation());
        ep.addModel(new insilico.eye_irritation_knn.ismEyeIrritationKnn());
        ep.addModel(new insilico.eye_irritation_sarpy.ismEyeIrritationSarpy());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Chromosomal aberration", SECTION_HUMAN);
        ep.addModel(new insilico.chromosomal_coral.ismChromosomalAberrationCoral());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Micronucleus assay", SECTION_HUMAN);
        ep.addModel(new insilico.micronculeus_vitro.ismMicronucleusInVitro());
        ep.addModel(new insilico.micronuclueus_vivo.ismMicronucleusInVivo());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Estrogen receptor effect", SECTION_HUMAN);
        ep.addModel(new insilico.rba_cerapp.ismEstrogenBindingCerapp());
        ep.addModel(new insilico.rba_irfmn.ismRbaIRFMN());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Androgen receptor effect", SECTION_HUMAN);
        ep.addModel(new insilico.rba_compara_irfmn.ismAndrogenBindingComparaIRFMN());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Thyroid receptor effect", SECTION_HUMAN);
        ep.addModel(new insilico.thyroid_tralpha_nrmea.ismTRAlphaNRMEA());
        ep.addModel(new insilico.thyroid_trbeta_nrmea.ismTRBetaNRMEA());
        ep.addModel(new insilico.ttr.ismTTR());
        ep.addModel(new insilico.dio1.ismDio1());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Glucocorticoid Receptor effect", SECTION_HUMAN);
        ep.addModel(new insilico.glucocorticoid_receptor.ismGlucocorticoidReceptor());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Thyroperoxidase Inhibitory activity", SECTION_HUMAN);
        ep.addModel(new insilico.tpo_oberon.ismTpoOberon());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Steroidogenesis activity", SECTION_HUMAN);
        ep.addModel(new insilico.steroidogenesisedscreen.ismSteroidogenesisEDScreen());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Endocrine Disruptor activity", SECTION_HUMAN);
        ep.addModel(new insilico.endocrine_disruptors_irfmn.ismEndocrineDisruptorsIRFMN());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("NOAEL", SECTION_HUMAN);
        ep.addModel(new insilico.noael_general_coral.ismNoaelGeneralCoral());
        ep.addModel(new insilico.noel_coral.ismNoaelCoral());
        ep.addModel(new insilico.noael_coral_liver.ismNoaelCoralLiver());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("LOAEL", SECTION_HUMAN);
        ep.addModel(new insilico.loael_general_coral.ismLoaelGeneralCoral());
        ep.addModel(new insilico.loael_coral_liver.ismLoaelCoralLiver());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Cramer classification", SECTION_HUMAN);
        ep.addModel(new insilico.cramer_toxtree.ismCramerToxtree());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Hepatotoxicity", SECTION_HUMAN);
        ep.addModel(new insilico.hepatoxicty_irfmn.ismHepatotoxicityIrfmn());
        ep.addModel(new ismDiliBayer(true, null));
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Cardiotoxicity", SECTION_HUMAN);
        ep.addModel(new ApicalCardioTox(true, null));
        ep.addModel(new CardioToxMultitask(true, null));
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Mitochondrial dysfunction", SECTION_HUMAN);
        ep.addModel(new MitochondrialDysfunction(true, null));
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Kidney failure (KF)", SECTION_HUMAN);
        ep.addModel(new ismOntoxAssay(true, null, ACE_ONTOX));
        ep.addModel(new ismOntoxAssay(true, null, OAT1_ONTOX));
        ep.addModel(new ismOntoxAssay(true, null, COX1_ONTOX));
        ep.addModel(new ismOntoxAssay(true, null, AT1R_ONTOX));
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Cholestasis  (CHO)", SECTION_HUMAN);
        ep.addModel(new ismOntoxAssay(true, null, BSEP_ONTOX));
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Neural tube closure (NTD)", SECTION_HUMAN);
        ep.addModel(new ismOntoxAssay(true, null, HDEAC_ONTOX));
        ep.addModel(new ismOntoxAssay(true, null, CYP26_ONTOX));
        ep.addModel(new ismOntoxAssay(true, null, BMP_ONTOX));
        ep.addModel(new ismOntoxAssay(true, null, WNT_ONTOX));
        ep.addModel(new ismOntoxAssay(true, null, FGFR1_ONTOX));
        ep.addModel(new ismOntoxAssay(true, null, FGFR2_ONTOX));
        ep.addModel(new ismOntoxAssay(true, null, FGFR3_ONTOX));
        ep.addModel(new ismOntoxAssay(true, null, FGFR4_ONTOX));
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Cognitive function defects (CFD)", SECTION_HUMAN);
        ep.addModel(new ismOntoxAssay(true, null, TTR_ONTOX));
        ep.addModel(new ismOntoxAssay(true, null, ACHE_ONTOX));
        ep.addModel(new ismOntoxAssay(true, null, THRB_ONTOX));
        ep.addModel(new ismOntoxAssay(true, null, THRA_ONTOX));
        ep.addModel(new ismOntoxAssay(true, null, VGSC_ONTOX));
        ep.addModel(new ismOntoxAssay(true, null, NMDA_ONTOX));
        ep.addModel(new ismOntoxAssay(true, null, PPARD_ONTOX));
        ep.addModel(new ismOntoxAssay(true, null, GR_ONTOX));
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Liver steatosis (STE)", SECTION_HUMAN);
        ep.addModel(new ismOntoxAssay(true, null, PXR_ONTOX));
        ep.addModel(new ismOntoxAssay(true, null, AHR_ONTOX));
        ep.addModel(new ismOntoxAssay(true, null, PPARA_ONTOX));
        ep.addModel(new ismOntoxAssay(true, null, PPARG_ONTOX));
        endpointsList.add(ep);

        // Ecotox

        ep = new VegaEndpointWithClass("BCF", SECTION_ECOTOX);
        ep.addModel(new insilico.bcf_caesar.ismBCFCaesar());
        ep.addModel(new insilico.bcf_meylan.ismBCFMeylan());
        ep.addModel(new insilico.bcf_arnotgobas.ismBCFArnotGobas());
        ep.addModel(new insilico.bcf_knn.ismBCFKnn());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Aquatic Acute Toxicity", SECTION_ECOTOX);
        ep.addModel(new insilico.fish_lc50.ismFishLC50());
        ep.addModel(new insilico.fish_nic.ismFishNic());
        ep.addModel(new insilico.fish_knn.ismFishKnn());
        ep.addModel(new insilico.fish_irfmn.ismFishIRFMN());
        ep.addModel(new insilico.fish_combase.ismFishCombase());
        ep.addModel(new insilico.fathead_epa.ismFatheadEPA());
        ep.addModel(new insilico.fathead_knn.ismFatheadKnn());
        ep.addModel(new insilico.daphnia_ec50.ismDaphniaEC50());
        ep.addModel(new insilico.daphnia_epa.ismDaphniaEPA());
        ep.addModel(new insilico.daphnia_demetra.ismDaphniaDemetra());
        ep.addModel(new insilico.daphnia_combase.ismDaphniaCombase());
        ep.addModel(new insilico.guppy_knn.ismGuppyKnn());
        ep.addModel(new insilico.algae_ec50.ismAlgaeEC50());
        ep.addModel(new insilico.algae_combaseclass.ismAlgaeCombaseClass());
        ep.addModel(new insilico.algae_combaseEC50.ismAlgaeCombaseEC50());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Aquatic Chronic Toxicity", SECTION_ECOTOX);
        ep.addModel(new insilico.fish_noec.ismFishNOEC());
        ep.addModel(new insilico.daphnia_noec.ismDaphniaNOEC());
        ep.addModel(new insilico.algae_noec.ismAlgaeNOEC());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Mode of Action", SECTION_ECOTOX);
        ep.addModel(new insilico.verhaar_toxtree.ismVerhaarToxtree());
        ep.addModel(new insilico.moa_epa.ismMoaEpa());
        ep.addModel(new insilico.moa_irfmn.ismMoaIrfmn());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Terrestrial Acute Toxicity", SECTION_ECOTOX);
        ep.addModel(new insilico.bee_knn.ismBeeKnn());
        ep.addModel(new insilico.earthworm_toxicity.ismEarthworkToxicity());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Sludge Toxicity", SECTION_ECOTOX);
        ep.addModel(new insilico.sludge_combaseclass.ismSludgeCombaseClass());
        ep.addModel(new insilico.sludge_combaseEC50.ismSludgeCombaseEC50());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Zebrafish embryo activity", SECTION_ECOTOX);
        ep.addModel(new insilico.zebrafish_coral.ismZebrafishCoral());
        endpointsList.add(ep);


        // Fate and Distribution

        ep = new VegaEndpointWithClass("Ready biodegradability", SECTION_FATE);
        ep.addModel(new insilico.readybio_irfmn.ismReadyBioIRFMN());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Persistence (sediment)", SECTION_FATE);
        ep.addModel(new insilico.persistence_sediment_irfmn.ismPersistenceSedimentIrfmn());
        ep.addModel(new insilico.persistence_sediment_quantitative_irfmn.ismPersistenceSedimentQuantitativeIrfmn());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Persistence (soil)", SECTION_FATE);
        ep.addModel(new insilico.persistence_soil_irfmn.ismPersistenceSoilIrfmn());
        ep.addModel(new insilico.persistence_soil_quantitative_irfmn.ismPersistenceSoilQuantitativeIrfmn());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Persistence (water)", SECTION_FATE);
        ep.addModel(new insilico.persistence_water_irfmn.ismPersistenceWaterIrfmn());
        ep.addModel(new insilico.persistence_quantative_water_irfmn.ismPersistenceWaterQuantitativeIrfmn());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Persistence (air)", SECTION_FATE);
        ep.addModel(new insilico.persistence_air_coral.ismPersistenceAirCoral());
        endpointsList.add(ep);


        // Physical Chemical properties

        ep = new VegaEndpointWithClass("Octanol/Water partition coefficient (logP)", SECTION_PHYS);
        ep.addModel(new insilico.meylanlogp.ismLogPMeylan());
        ep.addModel(new insilico.logp_mlogp.ismLogPMLogP());
        ep.addModel(new insilico.logp_alogp.ismLogPALogP());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Water solubility", SECTION_PHYS);
        ep.addModel(new insilico.watersolubility.ismWaterSolubilityIRFMN());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Vapour pressure", SECTION_PHYS);
        ep.addModel(new insilico.vapour_pressure.ismVapourPressure());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Melting point", SECTION_PHYS);
        ep.addModel(new insilico.melting_point.ismMeltingPoint());
        ep.addModel(new insilico.melting_point_knn.ismMeltingPointKnn());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Hydrolysis", SECTION_PHYS);
        ep.addModel(new insilico.hydrolysis_coral.ismHydrolysisCoral());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Henry's law constant", SECTION_PHYS);
        ep.addModel(new insilico.henryslaw.ismHenrysLawOpera());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Octanol/air partition coefficient (KOA)", SECTION_PHYS);
        ep.addModel(new insilico.koa_opera.ismKoaOpera());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Soil adsorption coefficient of organic compounds (KOC)", SECTION_PHYS);
        ep.addModel(new insilico.koc_opera.ismKocOpera());
        endpointsList.add(ep);


        // Human PBPK

        ep = new VegaEndpointWithClass("Plasma Protein Binding", SECTION_HUMAN_PBPK);
        ep.addModel(new insilico.logk.ismLogK());
        ep.addModel(new insilico.ppb_coral.ismPPBCoral());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Aromatase activity", SECTION_HUMAN_PBPK);
        ep.addModel(new insilico.aromatase_irfmn.ismAromataseIRFMN());
        ep.addModel(new insilico.aromatase_activity.ismAromataseTox21());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("P-Glycoprotein activity", SECTION_HUMAN_PBPK);
        ep.addModel(new insilico.pgp_nic.ismPgpNic());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Hepatic Steatosis MIE", SECTION_HUMAN_PBPK);
        ep.addModel(new insilico.pxr_up.ismPxrUp());
        ep.addModel(new insilico.pparg_up.ismPPARGup());
        ep.addModel(new insilico.ppara_up.ismPPARAUp());
        ep.addModel(new insilico.nrf2_up.ismNRF2Up());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Skin permeation (LogKp)", SECTION_HUMAN_PBPK);
        ep.addModel(new insilico.skin_permeation_potts.ismSkinPermeationPotts());
        ep.addModel(new insilico.skin_permeation_tenberge.ismSkinPermeationTenBerge());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Adipose tissue-blood partition", SECTION_HUMAN_PBPK);
        ep.addModel(new insilico.tissueblood_ineris.ismTissueBloodIneris());
        endpointsList.add(ep);

        ep = new VegaEndpointWithClass("Body elimination half-life", SECTION_HUMAN_PBPK);
        ep.addModel(new insilico.totalhl_qsarins.ismTotalHLQsarins());
        endpointsList.add(ep);


        // Eco PBPK

        ep = new VegaEndpointWithClass("kM/Half Life", SECTION_ECO_PBPK);
        ep.addModel(new insilico.km_arnot.ismKmArnot());
        endpointsList.add(ep);

        return endpointsList;
    }


    /**
     *  Return the instantiated class for an InsilicoModel given its tag
     *
     * @param Tag
     * @return
     * @throws Exception
     * @throws InitFailureException
     */
    public static InsilicoModel GetModelFromTag(String Tag) throws ModelNotFoundException, InitFailureException, PythonModelResourceNotFoundException, InitFailurePythonException {

        InsilicoModel selectedModel = null;
        switch (Tag) {
            case MUTA_CAESAR:
                selectedModel = new insilico.mutagenicity_caesar.ismMutagenicityCaesar();
                break;
            case MUTA_ISS:
                selectedModel = new insilico.mutagenicity_bb.ismMutagenicityBB();
                break;
            case MUTA_SARPY:
                selectedModel = new insilico.mutagenicity_sarpy.ismMutagenicitySarpy();
                break;
            case MUTA_KNN:
                selectedModel = new insilico.mutagenicity_knn.ismMutagenicityKnn();
                break;
            case MUTA_AMINES:
                selectedModel = new insilico.mutagenicity_amines.ismMutagenicityAmines();
                break;
            case DEVTOX_CAESAR:
                selectedModel = new insilico.devtox_caesar.ismDevtoxCaesar();
                break;
            case DEVTOX_PG:
                selectedModel = new insilico.devtox_pg.ismDevToxPG();
                break;
            case CARC_CAESAR:
                selectedModel = new insilico.carcinogenicity_caesar.ismCarcinogenicityCaesar();
                break;
            case CARC_ISS:
                selectedModel = new insilico.carcinogenicity_bb.ismCarcinogenicityBB();
                break;
            case CARC_ISSCAN_CGX:
                selectedModel = new insilico.carcinogenicity_isscancgx.ismCarcinogenicityIsscanCgx();
                break;
            case CARC_ANTARES:
                selectedModel = new insilico.carcinogenicity_antares.ismCarcinogenicityAntares();
                break;
            case CARC_SFO_CLASS:
                selectedModel = new insilico.carcinogenicity_sfoclassification.ismCarcinogenicitySFOClassification();
                break;
            case CARC_SFO_REGR:
                selectedModel = new insilico.carcinogenicity_sforegression.ismCarcinogenicitySFORegression();
                break;
            case CARC_SFI_CLASS:
                selectedModel = new insilico.carcinogenicity_sfi_classification.ismCarcinogenicitySFIClassification();
                break;
            case CARC_SFI_REGR:
                selectedModel = new insilico.carcinogenicity_sfiregression.ismCarcinogenicitySFIRegression();
                break;
            case CARCINOGENICTY_MALE:
                selectedModel = new insilico.carcinogenicity_rat_male.ismCarcinogenicityRatMale();
                break;
            case CARCINOGENICTY_FEMALE:
                selectedModel = new insilico.carcinogenicity_rat_female.ismCarcinogenicityRatFemale();
                break;
            case LD50_KNN:
                selectedModel = new insilico.ld50.ismLD50();
                break;
            case SKIN_CAESAR:
                selectedModel = new insilico.skin_caesar.ismSkinCaesar();
                break;
            case SKIN_IRFMN:
                selectedModel = new insilico.skin_irfmn.ismSkinIRFMN();
                break;
            case SKIN_NCSTOX:
                selectedModel = new insilico.skin_cosmetics.ismSkinCosmetics();
                break;
            case SKIN_SENSITIZATION_TOXTREE:
                selectedModel = new insilico.skin_sensitization_toxtree.ismSkinSensitizationToxTree();
                break;
            case SKIN_SENSITIZATION_CONCERT:
                selectedModel = new insilico.skin_sensitization_concert.ismSkinSensitizationConcert();
                break;
            case SKIN_SENSITIZATION_SARPY:
                selectedModel = new insilico.skin_sensitization_sarpy.ismSkinSensitizationSarpy();
                break;
            case SKIN_IRRITATION:
                selectedModel = new insilico.skin_irritation.ismSkinIrritation();
                break;
            case SKIN_IRRITATION_CORAL:
                selectedModel = new insilico.skin_irritation_coral.ismSkinIrritationCoral();
                break;
            case SKIN_IRRITATION_SARPY:
                selectedModel = new insilico.skin_irritation_sarpy.ismSkinIrritationSarpy();
                break;
            case EYE_IRRITATION:
                selectedModel = new insilico.eye_irritation.ismEyeIrritation();
                break;
            case EYE_IRRITATION_KNN:
                selectedModel = new insilico.eye_irritation_knn.ismEyeIrritationKnn();
                break;
            case EYE_IRRITATION_SARPY:
                selectedModel = new insilico.eye_irritation_sarpy.ismEyeIrritationSarpy();
                break;
            case CHROM_CORAL:
                selectedModel = new insilico.chromosomal_coral.ismChromosomalAberrationCoral();
                break;
            case MNVITRO_VERMEER:
                selectedModel = new insilico.micronculeus_vitro.ismMicronucleusInVitro();
                break;
            case MNVIVO_IRFMN:
                selectedModel = new insilico.micronuclueus_vivo.ismMicronucleusInVivo();
                break;
            case ESTROGEN_CERAPP:
                selectedModel = new insilico.rba_cerapp.ismEstrogenBindingCerapp();
                break;
            case RBA_IRFMN:
                selectedModel = new insilico.rba_irfmn.ismRbaIRFMN();
                break;
            case ANDROGEN_COMPARA:
                selectedModel = new insilico.rba_compara_irfmn.ismAndrogenBindingComparaIRFMN();
                break;
            case TRALPHA_NRMEA:
                selectedModel = new insilico.thyroid_tralpha_nrmea.ismTRAlphaNRMEA();
                break;
            case TRBETA_NRMEA:
                selectedModel = new insilico.thyroid_trbeta_nrmea.ismTRBetaNRMEA();
                break;
            case GLUCO_RECEPTOR:
                selectedModel = new insilico.glucocorticoid_receptor.ismGlucocorticoidReceptor();
                break;
            case TPO_OBERON:
                selectedModel = new insilico.tpo_oberon.ismTpoOberon();
                break;
            case ED_SCREEN:
                selectedModel = new insilico.endocrine_disruptors_irfmn.ismEndocrineDisruptorsIRFMN();
                break;
            case NOAEL_CONCERT_CORAL:
                selectedModel = new insilico.noael_general_coral.ismNoaelGeneralCoral();
                break;
            case NOAEL_CORAL:
                selectedModel = new insilico.noel_coral.ismNoaelCoral();
                break;
            case NOAEL_LIVER_CORAL:
                selectedModel = new insilico.noael_coral_liver.ismNoaelCoralLiver();
                break;
            case LOAEL_CONCERT_CORAL:
                selectedModel = new insilico.loael_general_coral.ismLoaelGeneralCoral();
                break;
            case LOEL_LIVER_CORAL:
                selectedModel = new insilico.loael_coral_liver.ismLoaelCoralLiver();
                break;
            case CRAMER_TOXTREE:
                selectedModel = new insilico.cramer_toxtree.ismCramerToxtree();
                break;
            case HEPA_IRFMN:
                selectedModel = new insilico.hepatoxicty_irfmn.ismHepatotoxicityIrfmn();
                break;
            case BCF_CAESAR:
                selectedModel = new insilico.bcf_caesar.ismBCFCaesar();
                break;
            case BCF_MEYLAN:
                selectedModel = new insilico.bcf_meylan.ismBCFMeylan();
                break;
            case BCF_ARNOTGOBAS:
                selectedModel = new insilico.bcf_arnotgobas.ismBCFArnotGobas();
                break;
            case BCF_KNN:
                selectedModel = new insilico.bcf_knn.ismBCFKnn();
                break;
            case FISH_LC50:
                selectedModel = new insilico.fish_lc50.ismFishLC50();
                break;
            case FISH_NIC:
                selectedModel = new insilico.fish_nic.ismFishNic();
                break;
            case FISH_KNN:
                selectedModel = new insilico.fish_knn.ismFishKnn();
                break;
            case FISH_IRFMN:
                selectedModel = new insilico.fish_irfmn.ismFishIRFMN();
                break;
            case FISH_COMBASE:
                selectedModel = new insilico.fish_combase.ismFishCombase();
                break;
            case FATHEAD_EPA:
                selectedModel = new insilico.fathead_epa.ismFatheadEPA();
                break;
            case FATHEAD_KNN:
                selectedModel = new insilico.fathead_knn.ismFatheadKnn();
                break;
            case DAPHNIA_EC50:
                selectedModel = new insilico.daphnia_ec50.ismDaphniaEC50();
                break;
            case DAPHNIA_EPA:
                selectedModel = new insilico.daphnia_epa.ismDaphniaEPA();
                break;
            case DAPHNIA_DEMETRA:
                selectedModel = new insilico.daphnia_demetra.ismDaphniaDemetra();
                break;
            case DAPHNIA_COMBASE:
                selectedModel = new insilico.daphnia_combase.ismDaphniaCombase();
                break;
            case GUPPY_KNN:
                selectedModel = new insilico.guppy_knn.ismGuppyKnn();
                break;
            case ALGAE_EC50:
                selectedModel = new insilico.algae_ec50.ismAlgaeEC50();
                break;
            case ALGAE_COMBASECLASS:
                selectedModel = new insilico.algae_combaseclass.ismAlgaeCombaseClass();
                break;
            case ALGAE_COMBASEEC50:
                selectedModel = new insilico.algae_combaseEC50.ismAlgaeCombaseEC50();
                break;
            case FISH_NOEC:
                selectedModel = new insilico.fish_noec.ismFishNOEC();
                break;
            case DAPHNIA_NOEC:
                selectedModel = new insilico.daphnia_noec.ismDaphniaNOEC();
                break;
            case ALGAE_NOEC:
                selectedModel = new insilico.algae_noec.ismAlgaeNOEC();
                break;
            case VERHAAR_TOXTREE:
                selectedModel = new insilico.verhaar_toxtree.ismVerhaarToxtree();
                break;
            case MOA_EPA:
                selectedModel = new insilico.moa_epa.ismMoaEpa();
                break;
            case MOA_IRFMN:
                selectedModel = new insilico.moa_irfmn.ismMoaIrfmn();
                break;
            case BEE_KNN:
                selectedModel = new insilico.bee_knn.ismBeeKnn();
                break;
            case EW_TOXICITY:
                selectedModel = new insilico.earthworm_toxicity.ismEarthworkToxicity();
                break;
            case SLUDGE_COMBASECLASS:
                selectedModel = new insilico.sludge_combaseclass.ismSludgeCombaseClass();
                break;
            case SLUDGE_COMBASEEC50:
                selectedModel = new insilico.sludge_combaseEC50.ismSludgeCombaseEC50();
                break;
            case ZEBRAFISH_CORAL:
                selectedModel = new insilico.zebrafish_coral.ismZebrafishCoral();
                break;
            case READYBIO_IRFMN:
                selectedModel = new insilico.readybio_irfmn.ismReadyBioIRFMN();
                break;
            case PERS_SED:
                selectedModel = new insilico.persistence_sediment_irfmn.ismPersistenceSedimentIrfmn();
                break;
            case PERS_SED_QUANT:
                selectedModel = new insilico.persistence_sediment_quantitative_irfmn.ismPersistenceSedimentQuantitativeIrfmn();
                break;
            case PERS_SOIL:
                selectedModel = new insilico.persistence_soil_irfmn.ismPersistenceSoilIrfmn();
                break;
            case PERS_SOIL_QUANT:
                selectedModel = new insilico.persistence_soil_quantitative_irfmn.ismPersistenceSoilQuantitativeIrfmn();
                break;
            case PERS_WAT:
                selectedModel = new insilico.persistence_water_irfmn.ismPersistenceWaterIrfmn();
                break;
            case PERS_WATER_QUANT:
                selectedModel = new insilico.persistence_quantative_water_irfmn.ismPersistenceWaterQuantitativeIrfmn();
                break;
            case PERS_AIR_CORAL:
                selectedModel = new insilico.persistence_air_coral.ismPersistenceAirCoral();
                break;
            case LOGP_MEYLAN:
                selectedModel = new insilico.meylanlogp.ismLogPMeylan();
                break;
            case LOGP_MLOGP:
                selectedModel = new insilico.logp_mlogp.ismLogPMLogP();
                break;
            case LOGP_ALOGP:
                selectedModel = new insilico.logp_alogp.ismLogPALogP();
                break;
            case WS_IRFMN:
                selectedModel = new insilico.watersolubility.ismWaterSolubilityIRFMN();
                break;
            case VAPOUR_PRESSURE:
                selectedModel = new insilico.vapour_pressure.ismVapourPressure();
                break;
            case MELTING_POINT:
                selectedModel = new insilico.melting_point.ismMeltingPoint();
                break;
            case MELTING_POINT_KNN:
                selectedModel = new insilico.melting_point_knn.ismMeltingPointKnn();
                break;
            case HYDROLYSIS_CORAL:
                selectedModel = new insilico.hydrolysis_coral.ismHydrolysisCoral();
                break;
            case HENRY_OPERA:
                selectedModel = new insilico.henryslaw.ismHenrysLawOpera();
                break;
            case KOA_OPERA:
                selectedModel = new insilico.koa_opera.ismKoaOpera();
                break;
            case KOC_OPERA:
                selectedModel = new insilico.koc_opera.ismKocOpera();
                break;
            case PPB_LOGK:
                selectedModel = new insilico.logk.ismLogK();
                break;
            case PPB_CORAL:
                selectedModel = new insilico.ppb_coral.ismPPBCoral();
                break;
            case AROM_IRMFN:
                selectedModel = new insilico.aromatase_irfmn.ismAromataseIRFMN();
                break;
            case AROM_TOX21:
                selectedModel = new insilico.aromatase_activity.ismAromataseTox21();
                break;
            case PGP_NIC:
                selectedModel = new insilico.pgp_nic.ismPgpNic();
                break;
            case HEPA_PXRUP:
                selectedModel = new insilico.pxr_up.ismPxrUp();
                break;
            case HEPA_PPARGUP:
                selectedModel = new insilico.pparg_up.ismPPARGup();
                break;
            case HEPA_PPARAUP:
                selectedModel = new insilico.ppara_up.ismPPARAUp();
                break;
            case HEPA_NRF2:
                selectedModel = new insilico.nrf2_up.ismNRF2Up();
                break;
            case SKINPERM_POTTS:
                selectedModel = new insilico.skin_permeation_potts.ismSkinPermeationPotts();
                break;
            case SKINPERM_TENBERGE:
                selectedModel = new insilico.skin_permeation_tenberge.ismSkinPermeationTenBerge();
                break;
            case TISSUEBLOOD_INERIS:
                selectedModel = new insilico.tissueblood_ineris.ismTissueBloodIneris();
                break;
            case TOTALHL_QSARINS:
                selectedModel = new insilico.totalhl_qsarins.ismTotalHLQsarins();
                break;
            case KM_ARNOT:
                selectedModel = new insilico.km_arnot.ismKmArnot();
                break;
            case DILI_BAYER:
                selectedModel = new ismDiliBayer();
                break;
            case APICAL_CARDIO_TOX:
                selectedModel = new ApicalCardioTox();
                break;
            case MITOCHONDRIAL_DYSFUNCTION:
                selectedModel = new MitochondrialDysfunction();
                break;
            case CARDIO_TOX_MULTITASK:
                selectedModel = new CardioToxMultitask();
                break;
            case ACE_ONTOX:
                selectedModel = new ismOntoxAssay(ACE_ONTOX);
                break;
            case NMDA_ONTOX:
                selectedModel = new ismOntoxAssay(NMDA_ONTOX);
                break;
            case PXR_ONTOX:
                selectedModel = new ismOntoxAssay(PXR_ONTOX);
                break;
            case ACHE_ONTOX:
                selectedModel = new ismOntoxAssay(ACHE_ONTOX);
                break;
            case AHR_ONTOX:
                selectedModel = new ismOntoxAssay(AHR_ONTOX);
                break;
            case AT1R_ONTOX:
                selectedModel = new ismOntoxAssay(AT1R_ONTOX);
                break;
            case BMP_ONTOX:
                selectedModel = new ismOntoxAssay(BMP_ONTOX);
                break;
            case BSEP_ONTOX:
                selectedModel = new ismOntoxAssay(BSEP_ONTOX);
                break;
            case COX1_ONTOX:
                selectedModel = new ismOntoxAssay(COX1_ONTOX);
                break;
            case CYP26_ONTOX:
                selectedModel = new ismOntoxAssay(CYP26_ONTOX);
                break;
            case FGFR1_ONTOX:
                selectedModel = new ismOntoxAssay(FGFR1_ONTOX);
                break;
            case FGFR2_ONTOX:
                selectedModel = new ismOntoxAssay(FGFR2_ONTOX);
                break;
            case FGFR3_ONTOX:
                selectedModel = new ismOntoxAssay(FGFR3_ONTOX);
                break;
            case FGFR4_ONTOX:
                selectedModel = new ismOntoxAssay(FGFR4_ONTOX);
                break;
            case GR_ONTOX:
                selectedModel = new ismOntoxAssay(GR_ONTOX);
                break;
            case HDEAC_ONTOX:
                selectedModel = new ismOntoxAssay(HDEAC_ONTOX);
                break;
            case OAT1_ONTOX:
                selectedModel = new ismOntoxAssay(OAT1_ONTOX);
                break;
            case PPARA_ONTOX:
                selectedModel = new ismOntoxAssay(PPARA_ONTOX);
                break;
            case PPARD_ONTOX:
                selectedModel = new ismOntoxAssay(PPARD_ONTOX);
                break;
            case PPARG_ONTOX:
                selectedModel = new ismOntoxAssay(PPARG_ONTOX);
                break;
            case THRA_ONTOX:
                selectedModel = new ismOntoxAssay(THRA_ONTOX);
                break;
            case THRB_ONTOX:
                selectedModel = new ismOntoxAssay(THRB_ONTOX);
                break;
            case TTR_ONTOX:
                selectedModel = new ismOntoxAssay(TTR_ONTOX);
                break;
            case VGSC_ONTOX:
                selectedModel = new ismOntoxAssay(VGSC_ONTOX);
                break;
            case WNT_ONTOX:
                selectedModel = new ismOntoxAssay(WNT_ONTOX);
                break;
            case DIO1_EDSCREEN:
                selectedModel = new ismDio1();
                break;
            case STERO_EDSCREEN:
                selectedModel = new ismSteroidogenesisEDScreen();
                break;
            case TTR_EDSCREEN:
                selectedModel = new ismTTR();
                break;

            default:
                throw new ModelNotFoundException("No model found for tag: " + Tag);
        }

        return selectedModel;
    }

    public static InsilicoModel GetModelFromTag(String Tag, iInsilicoModelRunnerMessenger messenger, boolean bypassCondaCheck) throws ModelNotFoundException, InitFailureException, GenericFailureException, PythonModelResourceNotFoundException, InitFailurePythonException{

        InsilicoModel selectedModel = null;
        switch (Tag) {
            case MUTA_CAESAR:
                selectedModel = new insilico.mutagenicity_caesar.ismMutagenicityCaesar();
                break;
            case MUTA_ISS:
                selectedModel = new insilico.mutagenicity_bb.ismMutagenicityBB();
                break;
            case MUTA_SARPY:
                selectedModel = new insilico.mutagenicity_sarpy.ismMutagenicitySarpy();
                break;
            case MUTA_KNN:
                selectedModel = new insilico.mutagenicity_knn.ismMutagenicityKnn();
                break;
            case MUTA_AMINES:
                selectedModel = new insilico.mutagenicity_amines.ismMutagenicityAmines();
                break;
            case DEVTOX_CAESAR:
                selectedModel = new insilico.devtox_caesar.ismDevtoxCaesar();
                break;
            case DEVTOX_PG:
                selectedModel = new insilico.devtox_pg.ismDevToxPG();
                break;
            case CARC_CAESAR:
                selectedModel = new insilico.carcinogenicity_caesar.ismCarcinogenicityCaesar();
                break;
            case CARC_ISS:
                selectedModel = new insilico.carcinogenicity_bb.ismCarcinogenicityBB();
                break;
            case CARC_ISSCAN_CGX:
                selectedModel = new insilico.carcinogenicity_isscancgx.ismCarcinogenicityIsscanCgx();
                break;
            case CARC_ANTARES:
                selectedModel = new insilico.carcinogenicity_antares.ismCarcinogenicityAntares();
                break;
            case CARC_SFO_CLASS:
                selectedModel = new insilico.carcinogenicity_sfoclassification.ismCarcinogenicitySFOClassification();
                break;
            case CARC_SFO_REGR:
                selectedModel = new insilico.carcinogenicity_sforegression.ismCarcinogenicitySFORegression();
                break;
            case CARC_SFI_CLASS:
                selectedModel = new insilico.carcinogenicity_sfi_classification.ismCarcinogenicitySFIClassification();
                break;
            case CARC_SFI_REGR:
                selectedModel = new insilico.carcinogenicity_sfiregression.ismCarcinogenicitySFIRegression();
                break;
            case CARCINOGENICTY_MALE:
                selectedModel = new insilico.carcinogenicity_rat_male.ismCarcinogenicityRatMale();
                break;
            case CARCINOGENICTY_FEMALE:
                selectedModel = new insilico.carcinogenicity_rat_female.ismCarcinogenicityRatFemale();
                break;
            case LD50_KNN:
                selectedModel = new insilico.ld50.ismLD50();
                break;
            case SKIN_CAESAR:
                selectedModel = new insilico.skin_caesar.ismSkinCaesar();
                break;
            case SKIN_IRFMN:
                selectedModel = new insilico.skin_irfmn.ismSkinIRFMN();
                break;
            case SKIN_NCSTOX:
                selectedModel = new insilico.skin_cosmetics.ismSkinCosmetics();
                break;
            case SKIN_SENSITIZATION_TOXTREE:
                selectedModel = new insilico.skin_sensitization_toxtree.ismSkinSensitizationToxTree();
                break;
            case SKIN_SENSITIZATION_CONCERT:
                selectedModel = new insilico.skin_sensitization_concert.ismSkinSensitizationConcert();
                break;
            case SKIN_SENSITIZATION_SARPY:
                selectedModel = new insilico.skin_sensitization_sarpy.ismSkinSensitizationSarpy();
                break;
            case SKIN_IRRITATION:
                selectedModel = new insilico.skin_irritation.ismSkinIrritation();
                break;
            case SKIN_IRRITATION_CORAL:
                selectedModel = new insilico.skin_irritation_coral.ismSkinIrritationCoral();
                break;
            case SKIN_IRRITATION_SARPY:
                selectedModel = new insilico.skin_irritation_sarpy.ismSkinIrritationSarpy();
                break;
            case EYE_IRRITATION:
                selectedModel = new insilico.eye_irritation.ismEyeIrritation();
                break;
            case EYE_IRRITATION_KNN:
                selectedModel = new insilico.eye_irritation_knn.ismEyeIrritationKnn();
                break;
            case EYE_IRRITATION_SARPY:
                selectedModel = new insilico.eye_irritation_sarpy.ismEyeIrritationSarpy();
                break;
            case CHROM_CORAL:
                selectedModel = new insilico.chromosomal_coral.ismChromosomalAberrationCoral();
                break;
            case MNVITRO_VERMEER:
                selectedModel = new insilico.micronculeus_vitro.ismMicronucleusInVitro();
                break;
            case MNVIVO_IRFMN:
                selectedModel = new insilico.micronuclueus_vivo.ismMicronucleusInVivo();
                break;
            case ESTROGEN_CERAPP:
                selectedModel = new insilico.rba_cerapp.ismEstrogenBindingCerapp();
                break;
            case RBA_IRFMN:
                selectedModel = new insilico.rba_irfmn.ismRbaIRFMN();
                break;
            case ANDROGEN_COMPARA:
                selectedModel = new insilico.rba_compara_irfmn.ismAndrogenBindingComparaIRFMN();
                break;
            case TRALPHA_NRMEA:
                selectedModel = new insilico.thyroid_tralpha_nrmea.ismTRAlphaNRMEA();
                break;
            case TRBETA_NRMEA:
                selectedModel = new insilico.thyroid_trbeta_nrmea.ismTRBetaNRMEA();
                break;
            case GLUCO_RECEPTOR:
                selectedModel = new insilico.glucocorticoid_receptor.ismGlucocorticoidReceptor();
                break;
            case TPO_OBERON:
                selectedModel = new insilico.tpo_oberon.ismTpoOberon();
                break;
            case ED_SCREEN:
                selectedModel = new insilico.endocrine_disruptors_irfmn.ismEndocrineDisruptorsIRFMN();
                break;
            case NOAEL_CONCERT_CORAL:
                selectedModel = new insilico.noael_general_coral.ismNoaelGeneralCoral();
                break;
            case NOAEL_CORAL:
                selectedModel = new insilico.noel_coral.ismNoaelCoral();
                break;
            case NOAEL_LIVER_CORAL:
                selectedModel = new insilico.noael_coral_liver.ismNoaelCoralLiver();
                break;
            case LOAEL_CONCERT_CORAL:
                selectedModel = new insilico.loael_general_coral.ismLoaelGeneralCoral();
                break;
            case LOEL_LIVER_CORAL:
                selectedModel = new insilico.loael_coral_liver.ismLoaelCoralLiver();
                break;
            case CRAMER_TOXTREE:
                selectedModel = new insilico.cramer_toxtree.ismCramerToxtree();
                break;
            case HEPA_IRFMN:
                selectedModel = new insilico.hepatoxicty_irfmn.ismHepatotoxicityIrfmn();
                break;
            case BCF_CAESAR:
                selectedModel = new insilico.bcf_caesar.ismBCFCaesar();
                break;
            case BCF_MEYLAN:
                selectedModel = new insilico.bcf_meylan.ismBCFMeylan();
                break;
            case BCF_ARNOTGOBAS:
                selectedModel = new insilico.bcf_arnotgobas.ismBCFArnotGobas();
                break;
            case BCF_KNN:
                selectedModel = new insilico.bcf_knn.ismBCFKnn();
                break;
            case FISH_LC50:
                selectedModel = new insilico.fish_lc50.ismFishLC50();
                break;
            case FISH_NIC:
                selectedModel = new insilico.fish_nic.ismFishNic();
                break;
            case FISH_KNN:
                selectedModel = new insilico.fish_knn.ismFishKnn();
                break;
            case FISH_IRFMN:
                selectedModel = new insilico.fish_irfmn.ismFishIRFMN();
                break;
            case FISH_COMBASE:
                selectedModel = new insilico.fish_combase.ismFishCombase();
                break;
            case FATHEAD_EPA:
                selectedModel = new insilico.fathead_epa.ismFatheadEPA();
                break;
            case FATHEAD_KNN:
                selectedModel = new insilico.fathead_knn.ismFatheadKnn();
                break;
            case DAPHNIA_EC50:
                selectedModel = new insilico.daphnia_ec50.ismDaphniaEC50();
                break;
            case DAPHNIA_EPA:
                selectedModel = new insilico.daphnia_epa.ismDaphniaEPA();
                break;
            case DAPHNIA_DEMETRA:
                selectedModel = new insilico.daphnia_demetra.ismDaphniaDemetra();
                break;
            case DAPHNIA_COMBASE:
                selectedModel = new insilico.daphnia_combase.ismDaphniaCombase();
                break;
            case GUPPY_KNN:
                selectedModel = new insilico.guppy_knn.ismGuppyKnn();
                break;
            case ALGAE_EC50:
                selectedModel = new insilico.algae_ec50.ismAlgaeEC50();
                break;
            case ALGAE_COMBASECLASS:
                selectedModel = new insilico.algae_combaseclass.ismAlgaeCombaseClass();
                break;
            case ALGAE_COMBASEEC50:
                selectedModel = new insilico.algae_combaseEC50.ismAlgaeCombaseEC50();
                break;
            case FISH_NOEC:
                selectedModel = new insilico.fish_noec.ismFishNOEC();
                break;
            case DAPHNIA_NOEC:
                selectedModel = new insilico.daphnia_noec.ismDaphniaNOEC();
                break;
            case ALGAE_NOEC:
                selectedModel = new insilico.algae_noec.ismAlgaeNOEC();
                break;
            case VERHAAR_TOXTREE:
                selectedModel = new insilico.verhaar_toxtree.ismVerhaarToxtree();
                break;
            case MOA_EPA:
                selectedModel = new insilico.moa_epa.ismMoaEpa();
                break;
            case MOA_IRFMN:
                selectedModel = new insilico.moa_irfmn.ismMoaIrfmn();
                break;
            case BEE_KNN:
                selectedModel = new insilico.bee_knn.ismBeeKnn();
                break;
            case EW_TOXICITY:
                selectedModel = new insilico.earthworm_toxicity.ismEarthworkToxicity();
                break;
            case SLUDGE_COMBASECLASS:
                selectedModel = new insilico.sludge_combaseclass.ismSludgeCombaseClass();
                break;
            case SLUDGE_COMBASEEC50:
                selectedModel = new insilico.sludge_combaseEC50.ismSludgeCombaseEC50();
                break;
            case ZEBRAFISH_CORAL:
                selectedModel = new insilico.zebrafish_coral.ismZebrafishCoral();
                break;
            case READYBIO_IRFMN:
                selectedModel = new insilico.readybio_irfmn.ismReadyBioIRFMN();
                break;
            case PERS_SED:
                selectedModel = new insilico.persistence_sediment_irfmn.ismPersistenceSedimentIrfmn();
                break;
            case PERS_SED_QUANT:
                selectedModel = new insilico.persistence_sediment_quantitative_irfmn.ismPersistenceSedimentQuantitativeIrfmn();
                break;
            case PERS_SOIL:
                selectedModel = new insilico.persistence_soil_irfmn.ismPersistenceSoilIrfmn();
                break;
            case PERS_SOIL_QUANT:
                selectedModel = new insilico.persistence_soil_quantitative_irfmn.ismPersistenceSoilQuantitativeIrfmn();
                break;
            case PERS_WAT:
                selectedModel = new insilico.persistence_water_irfmn.ismPersistenceWaterIrfmn();
                break;
            case PERS_WATER_QUANT:
                selectedModel = new insilico.persistence_quantative_water_irfmn.ismPersistenceWaterQuantitativeIrfmn();
                break;
            case PERS_AIR_CORAL:
                selectedModel = new insilico.persistence_air_coral.ismPersistenceAirCoral();
                break;
            case LOGP_MEYLAN:
                selectedModel = new insilico.meylanlogp.ismLogPMeylan();
                break;
            case LOGP_MLOGP:
                selectedModel = new insilico.logp_mlogp.ismLogPMLogP();
                break;
            case LOGP_ALOGP:
                selectedModel = new insilico.logp_alogp.ismLogPALogP();
                break;
            case WS_IRFMN:
                selectedModel = new insilico.watersolubility.ismWaterSolubilityIRFMN();
                break;
            case VAPOUR_PRESSURE:
                selectedModel = new insilico.vapour_pressure.ismVapourPressure();
                break;
            case MELTING_POINT:
                selectedModel = new insilico.melting_point.ismMeltingPoint();
                break;
            case MELTING_POINT_KNN:
                selectedModel = new insilico.melting_point_knn.ismMeltingPointKnn();
                break;
            case HYDROLYSIS_CORAL:
                selectedModel = new insilico.hydrolysis_coral.ismHydrolysisCoral();
                break;
            case HENRY_OPERA:
                selectedModel = new insilico.henryslaw.ismHenrysLawOpera();
                break;
            case KOA_OPERA:
                selectedModel = new insilico.koa_opera.ismKoaOpera();
                break;
            case KOC_OPERA:
                selectedModel = new insilico.koc_opera.ismKocOpera();
                break;
            case PPB_LOGK:
                selectedModel = new insilico.logk.ismLogK();
                break;
            case PPB_CORAL:
                selectedModel = new insilico.ppb_coral.ismPPBCoral();
                break;
            case AROM_IRMFN:
                selectedModel = new insilico.aromatase_irfmn.ismAromataseIRFMN();
                break;
            case AROM_TOX21:
                selectedModel = new insilico.aromatase_activity.ismAromataseTox21();
                break;
            case PGP_NIC:
                selectedModel = new insilico.pgp_nic.ismPgpNic();
                break;
            case HEPA_PXRUP:
                selectedModel = new insilico.pxr_up.ismPxrUp();
                break;
            case HEPA_PPARGUP:
                selectedModel = new insilico.pparg_up.ismPPARGup();
                break;
            case HEPA_PPARAUP:
                selectedModel = new insilico.ppara_up.ismPPARAUp();
                break;
            case HEPA_NRF2:
                selectedModel = new insilico.nrf2_up.ismNRF2Up();
                break;
            case SKINPERM_POTTS:
                selectedModel = new insilico.skin_permeation_potts.ismSkinPermeationPotts();
                break;
            case SKINPERM_TENBERGE:
                selectedModel = new insilico.skin_permeation_tenberge.ismSkinPermeationTenBerge();
                break;
            case TISSUEBLOOD_INERIS:
                selectedModel = new insilico.tissueblood_ineris.ismTissueBloodIneris();
                break;
            case TOTALHL_QSARINS:
                selectedModel = new insilico.totalhl_qsarins.ismTotalHLQsarins();
                break;
            case KM_ARNOT:
                selectedModel = new insilico.km_arnot.ismKmArnot();
                break;
            case DILI_BAYER:
                selectedModel = new ismDiliBayer(bypassCondaCheck, messenger);
                break;
            case APICAL_CARDIO_TOX:
                selectedModel = new ApicalCardioTox(bypassCondaCheck, messenger);
                break;
            case MITOCHONDRIAL_DYSFUNCTION:
                selectedModel = new MitochondrialDysfunction(bypassCondaCheck, messenger);
                break;
            case CARDIO_TOX_MULTITASK:
                selectedModel = new CardioToxMultitask(bypassCondaCheck, messenger);
                break;
            case ACE_ONTOX:
                selectedModel = new ismOntoxAssay(bypassCondaCheck, messenger, ACE_ONTOX);
                break;
            case NMDA_ONTOX:
                selectedModel = new ismOntoxAssay(bypassCondaCheck, messenger, NMDA_ONTOX);
                break;
            case PXR_ONTOX:
                selectedModel = new ismOntoxAssay(bypassCondaCheck, messenger, PXR_ONTOX);
                break;
            case ACHE_ONTOX:
                selectedModel = new ismOntoxAssay(bypassCondaCheck, messenger, ACHE_ONTOX);
                break;
            case AHR_ONTOX:
                selectedModel = new ismOntoxAssay(bypassCondaCheck, messenger, AHR_ONTOX);
                break;
            case AT1R_ONTOX:
                selectedModel = new ismOntoxAssay(bypassCondaCheck, messenger, AT1R_ONTOX);
                break;
            case BMP_ONTOX:
                selectedModel = new ismOntoxAssay(bypassCondaCheck, messenger, BMP_ONTOX);
                break;
            case BSEP_ONTOX:
                selectedModel = new ismOntoxAssay(bypassCondaCheck, messenger, BSEP_ONTOX);
                break;
            case COX1_ONTOX:
                selectedModel = new ismOntoxAssay(bypassCondaCheck, messenger, COX1_ONTOX);
                break;
            case CYP26_ONTOX:
                selectedModel = new ismOntoxAssay(bypassCondaCheck, messenger, CYP26_ONTOX);
                break;
            case FGFR1_ONTOX:
                selectedModel = new ismOntoxAssay(bypassCondaCheck, messenger, FGFR1_ONTOX);
                break;
            case FGFR2_ONTOX:
                selectedModel = new ismOntoxAssay(bypassCondaCheck, messenger, FGFR2_ONTOX);
                break;
            case FGFR3_ONTOX:
                selectedModel = new ismOntoxAssay(bypassCondaCheck, messenger, FGFR3_ONTOX);
                break;
            case FGFR4_ONTOX:
                selectedModel = new ismOntoxAssay(bypassCondaCheck, messenger, FGFR4_ONTOX);
                break;
            case GR_ONTOX:
                selectedModel = new ismOntoxAssay(bypassCondaCheck, messenger, GR_ONTOX);
                break;
            case HDEAC_ONTOX:
                selectedModel = new ismOntoxAssay(bypassCondaCheck, messenger, HDEAC_ONTOX);
                break;
            case OAT1_ONTOX:
                selectedModel = new ismOntoxAssay(bypassCondaCheck, messenger, OAT1_ONTOX);
                break;
            case PPARA_ONTOX:
                selectedModel = new ismOntoxAssay(bypassCondaCheck, messenger, PPARA_ONTOX);
                break;
            case PPARD_ONTOX:
                selectedModel = new ismOntoxAssay(bypassCondaCheck, messenger, PPARD_ONTOX);
                break;
            case PPARG_ONTOX:
                selectedModel = new ismOntoxAssay(bypassCondaCheck, messenger, PPARG_ONTOX);
                break;
            case THRA_ONTOX:
                selectedModel = new ismOntoxAssay(bypassCondaCheck, messenger, THRA_ONTOX);
                break;
            case THRB_ONTOX:
                selectedModel = new ismOntoxAssay(bypassCondaCheck, messenger, THRB_ONTOX);
                break;
            case TTR_ONTOX:
                selectedModel = new ismOntoxAssay(bypassCondaCheck, messenger, TTR_ONTOX);
                break;
            case VGSC_ONTOX:
                selectedModel = new ismOntoxAssay(bypassCondaCheck, messenger, VGSC_ONTOX);
                break;
            case WNT_ONTOX:
                selectedModel = new ismOntoxAssay(bypassCondaCheck, messenger, WNT_ONTOX);
                break;
            case DIO1_EDSCREEN:
                selectedModel = new ismDio1();
                break;
            case STERO_EDSCREEN:
                selectedModel = new ismSteroidogenesisEDScreen();
                break;
            case TTR_EDSCREEN:
                selectedModel = new ismTTR();
                break;


            default:
                throw new ModelNotFoundException("No model found for tag: " + Tag);
        }

        return selectedModel;
    }


    /**
     * Return an instantiated class for an InsilicoModelConsensus given its tag
     *
     * @param Tag
     * @return
     * @throws Exception
     * @throws InitFailureException
     */
    public static InsilicoModelConsensus GetConsensusFromTag(String Tag) throws ModelNotFoundException, InitFailureException {

        InsilicoModelConsensus selectedModel = null;
        switch (Tag) {
            case MUTA_CONSENSUS:
                selectedModel = new insilico.mutagenicity_consensus.ismcMutagenicity();
                break;

            default:
                throw new ModelNotFoundException("No model found for tag: " + Tag);
        }

        return selectedModel;
    }
    
}
