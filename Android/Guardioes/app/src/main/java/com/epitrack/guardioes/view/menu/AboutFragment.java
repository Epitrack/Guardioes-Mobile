package com.epitrack.guardioes.view.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.view.base.BaseFragment;
import com.google.android.gms.analytics.HitBuilders;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Igor Morais
 */
public class AboutFragment extends BaseFragment {

    @Bind(R.id.message_about_content_01)
    TextView textViewAbout;

    @Bind(R.id.txt_version_build)
    TextView textViewVersionBuild;

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        getSupportActionBar().setTitle(R.string.about);
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("About Screen - " + this.getClass().getSimpleName());

        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup viewGroup, final Bundle bundle) {

        final View view = inflater.inflate(R.layout.about, viewGroup, false);

        ButterKnife.bind(this, view);

        textViewVersionBuild.setText("Versão " + SingleUser.getInstance().getVersionBuild());

        textViewAbout.setText(Html.fromHtml("<p>&nbsp;&nbsp;&#8226;&nbsp;O GUARDIÕES DA SAÚDE é um aplicativo projetado para dispositivos móveis e faz parte do projeto inovador de aprimoramento da vigilância em saúde no Sistema Único de Saúde (SUS), denominado Vigilância Participativa.</p>\n" +
                "\n" +
                "<p>&nbsp;&nbsp;&#8226;&nbsp;É um processo simples que conta com a participação voluntária de visitantes ou residentes no Brasil, informando sobre sua condição de saúde. O GUARDIÕES DA SAÚDE foi desenvolvido em português para ser um canal complementar de informação de saúde e de serviços aos usuários, permitindo a participação de todos. Para isso, basta ter 13 anos de idade ou mais e aceitar os Termos de Participação e Uso, informando regularmente sua condição de saúde, conforme instruções disponíveis no aplicativo.</p>\n" +
                "\n" +
                "<p>&nbsp;&nbsp;&#8226;&nbsp;Este projeto é uma iniciativa da Secretaria de Vigilância em Saúde, do Ministério da Saúde, em parceria com as Secretarias de Saúde das sedes dos jogos e outras instituições nacionais e internacionais.</p>\n" +
                "\n" +
                "<p>&nbsp;&nbsp;&#8226;&nbsp;Utilizada pela primeira vez no Brasil, a implementação da estratégia de Vigilância Participativa ocorrerá sem investimento público no desenvolvimento do software. Para isso, por meio de parceria, o aplicativo GUARDIÕES DA SAÚDE está sendo desenvolvido como software livre por meio de investimento da Skoll Global Threats Fund e apoio operacional da Training in Public Health Intervention Network (Tephinet). Ambas são Organizações não-governamentais sem fins lucrativos que atuam no aprimoramento da vigilância em saúde no mundo e em técnicas de Detecção Digital de Doenças. O desenvolvimento tecnológico do aplicativo é realizado por meio da startup Epitrack, sediada em Recife/Pernambuco.</p>\n" +
                "\n" +
                "<p>&nbsp;&nbsp;&#8226;&nbsp;Esta parceria também contempla o apoio técnico e a troca de experiências com outras iniciativas adotadas no mundo, como Flu Near You (flunearyou.org) e Salud Boricua (saludboricua.org), criados e administrados pela ONG HealthMap (healthmap.org) do Hospital da Criança de Boston em colaboração com a Associação Americana de Saúde Pública (apha.org) e pela Skoll Global Threats Fund (skollglobalthreats.org). Estas estratégias visam o monitoramento de síndrome gripal nos Estados Unidos da América (EUA) e Canadá e a ocorrência de dengue, leptospirose e síndrome gripal em Porto Rico.</p>\n" +
                "\n" +
                "<p><b>Saiba mais sobre a Vigilância Participativa</b></p>\n" +
                "\n" +
                "<p>&nbsp;&nbsp;&#8226;&nbsp;Sua participação é uma gentil contribuição para o aprimoramento da vigilância em saúde no Brasil. No cadastro, não há registro nominal e a privacidade do e-mail está garantida. Não haverá nenhum contato do sistema de saúde com você, em decorrência da participação, e a comunicação de sua condição de saúde não significa que estará realizando uma consulta médica.</p>\n" +
                "\n" +
                "<p>&nbsp;&nbsp;&#8226;&nbsp;É importante destacar que caso você informe que não está se sentindo bem, é recomendável que consulte um médico de referência ou serviço de saúde mais próximo de você. Para isso, está disponível no aplicativo um atalho com pesquisa pré-configurada de serviços de saúde e farmácias mais próximos de você, com base no serviço de geolocalização do seu dispositivo móvel no Google Maps. Além disso, você dispõe dos telefones úteis incluindo o do SAMU (Serviço de Atendimento Móvel de Urgência), cuja a ligação é gratuita e funciona 24 horas por dia atendendo solicitações de atendimento de urgência para ocorrências de natureza traumática, clínica, pediátrica, cirúrgica, gineco-obstétrica e de saúde mental, em residências, locais de trabalho e vias públicas.</p>\n" +
                "\n" +
                "<p><b>Veja os benefícios de sua participação</b></p>\n" +
                "\n" +
                "<p>&nbsp;&nbsp;&#8226;&nbsp;Participar ativamente das ações de vigilância em saúde que visam a prevenção de doenças na comunidade;</p>\n" +
                "\n" +
                "<p>&nbsp;&nbsp;&#8226;&nbsp;Acessar os contatos de serviços públicos úteis sem precisar alterar sua agenda de telefones;</p>\n" +
                "\n" +
                "<p>&nbsp;&nbsp;&#8226;&nbsp;Acessar facilmente informações úteis sobre cuidados de saúde;</p>\n" +
                "\n" +
                "<p>&nbsp;&nbsp;&#8226;&nbsp;Usufruir de facilidades como a pesquisa pré-configurada por Unidades de Pronto Atendimento e farmácias próximas (20 km) no Google Maps, sem necessidade de abrir outro aplicativo;</p>\n" +
                "\n" +
                "<p>&nbsp;&nbsp;&#8226;&nbsp;Acompanhar o perfil de saúde dos participantes por meio do website www.guardioesdasaude.org;</p>\n" +
                "\n" +
                "<p><b>Custo</b></p>\n" +
                "\n" +
                "<p>&nbsp;&nbsp;&#8226;&nbsp;Download e participação: não há nenhum custo para obter e participar do GUARDIÕES DA SAÚDE;</p>\n" +
                "\n" +
                "<p>&nbsp;&nbsp;&#8226;&nbsp;Acesso aos serviços sem custo: por meio de acesso gratuito em pontos wi-fi na cidade;</p>\n" +
                "\n" +
                "<p>&nbsp;&nbsp;&#8226;&nbsp;Acesso aos serviços com custo: por meio de seu pacote de dados contratado;</p>\n" +
                "\n" +
                "<p>&nbsp;&nbsp;&#8226;&nbsp;Chamada telefônica: Os serviços públicos de referência descritos nos telefones úteis são oferecidos por meio de chamada gratuita, conforme informação do órgão responsável;</p>"));

        return view;
    }
}
