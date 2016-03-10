package com.epitrack.guardioes.view.tip;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.google.android.gms.analytics.HitBuilders;

import butterknife.Bind;

/**
 * @author Miqueias Lopes
 */
public class ZikaTipActivity extends BaseAppCompatActivity {

    @Bind(R.id.zika_content)
    TextView zikaContent;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.zika_info);

        zikaContent.setText(Html.fromHtml("<p>&#8226;&nbsp;Os vírus da Dengue, Zika e Chikungunya são transmitidos pelo mosquito Aedes <i>aegypti</i> e apresentam sinais e sintomas parecidos.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;É importante que você procure o serviço de saúde mais próximo quando apresentar febre, manchas vermelhas pelo corpo, dores no corpo ou dor nas juntas (articulações) ou coceira.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Procure se informar se no seu bairro ou cidade estão acontecendo casos de Dengue, Zika ou Chikungunya.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;A Dengue é a doença mais comum entre as três, já sendo bem conhecida pela população brasileira.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Os primeiros casos de infecção pelo Chikungunya ocorreram em 2014 e os de Zika em fevereiro de 2015.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;No caso da infecção pelo vírus Zika, a maior parte das pessoas (mais de 80%) não apresenta sinais e sintomas. As que apresentam a doença não necessitam de atendimento médico e hospitalização.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;As complicações causadas pela infecção pelo vírus Zika são pouco conhecidas. Manifestações neurológicas (paralisia facial, fraqueza nas pernas e Síndrome de Guillain-Barré) raramente podem ocorrer alguns dias depois do quadro agudo. A infecção durante a gestação pode levar a malformações congênitas graves, em especial a microcefalia.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Não existe tratamento específico para a infecção pelo vírus Zika.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Se estiver com suspeita dessas doenças, tome líquidos em abundância.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Não é aconselhável o uso de medicamentos à base de ácido acetilsalicílico (ex.: AAS, Aspirina entre outros) e nem de anti-inflamatórios, em função do risco aumentado de complicações hemorrágicas (sangramento) no caso de doenças como a Dengue.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;A principal medida de prevenção é evitar a reprodução dos mosquitos Aedes <i>aegypti</i>, transmissores das três doenças: não deixar água parada em pratinhos de plantas, pneus, garrafas, copos ou outros recipientes ou depósitos; jogar o lixo na lixeira e tampar as caixas de água.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Os repelentes para uso sobre a pele podem ser utilizados por gestantes, desde que estejam devidamente registrados na ANVISA e que sejam seguidas as instruções de uso descritas no rótulo. Produtos à base de DEET, Icaridin (Picaridin), EBAAP (IR3535) são considerados seguros para serem utilizados por gestantes.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Produtos à base DEET não devem ser usados em crianças menores de 2 anos. Em crianças entre 2 e 12 anos, a concentração dever ser no máximo 10% e a aplicação deve se restringir a 3 vezes por dia. Para maiores de 12 anos, são permitidas as concentrações superiores a 10%.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Os repelentes de ambiente, como aqueles de parede (aparelhos elétricos) ou espirais não devem ser utilizados em locais com pouca ventilação nem na presença de pessoas asmáticas ou com alergias respiratórias. Estes podem ser utilizados em qualquer ambiente da casa desde que estejam, no mínimo, a 2 metros de distância das pessoas.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Atenção para sinais que podem indicar agravamento da Dengue: falta de ar, tontura, dores abdominais ou sangramentos. Independentemente de os sintomas anteriores terem sido sugestivos de Dengue, Zika e Chikungunya, frente a um desses sinais, procure um serviço de saúde imediatamente.</p>" +
                "\n" +
                "<p>&#8226;&nbsp;Para dúvidas referentes à Dengue, Zika e Chikungunya, favor ligar para o Disque Saúde, por meio do numero <b><a href=\"tel:136\">136</a></b> e digitar a opção 3.</p>"));

        zikaContent.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Zika Tip Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }
}