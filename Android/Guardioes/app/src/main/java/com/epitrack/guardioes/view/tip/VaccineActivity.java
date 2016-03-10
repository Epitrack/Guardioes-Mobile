package com.epitrack.guardioes.view.tip;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.google.android.gms.analytics.HitBuilders;

import butterknife.Bind;

/**
 * @author Igor Morais
 */
public class VaccineActivity extends BaseAppCompatActivity {

    @Bind(R.id.vacinne_content_01)
    TextView vaccineContent;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.vaccine);

        vaccineContent.setText(Html.fromHtml("<p>&#8226;&nbsp;A Organização Mundial da Saúde (OMS) informa que não há calendário internacional de vacinação para todos os viajantes. Para cada viajante, será adotada recomendação personalizada de acordo com os países a serem visitados, dependendo do tipo, duração e tempo disponível para aplicação da vacina antes da partida.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;O Brasil não exige o Certificado Internacional de Vacinação ou Profilaxia para entrada no país.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Certifique-se de que suas vacinas de rotina estejam em dia, de acordo com as recomendações de seu país de origem, pois é uma medida eficaz e segura para a prevenção de várias doenças.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Devido a circulação do sarampo na Europa, África e Ásia recomenda-se que pessoas com origem ou destino a países com ocorrência dessa doença estejam vacinados previamente.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;A preparação para a viagem é uma boa oportunidade para verificar o estado vacinal de bebês, crianças, adolescentes, adultos e idosos.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Todo morador ou visitante de qualquer cidade brasileira tem disponível também as vacinas para influenza e Pólio que são enfermidades relacionadas a possíveis emergências de saúde pública de importância internacional para a Organização Mundial de Saúde.</p>\n" +
                "\n" +
                "<p><b>Prevenção contra febre amarela</b></p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Previna-se contra febre amarela tomando a vacina 10 dias antes de visitar áreas de mata ou de praticar turismo ecológico ou rural.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Procure seu medico, pois ele poderá lhe ajudar a decidir se a vacina é necessária, com base em seu plano de viagem.</p>\n"));
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Vaccine Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }
}
