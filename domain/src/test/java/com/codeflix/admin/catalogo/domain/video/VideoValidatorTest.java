package com.codeflix.admin.catalogo.domain.video;

import com.codeflix.admin.catalogo.domain.castmember.CastMemberID;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.DomainException;
import com.codeflix.admin.catalogo.domain.genre.GenreID;
import com.codeflix.admin.catalogo.domain.validation.handlers.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

public class VideoValidatorTest {

    @Test
    public void givenNullTitle_whenCallingValidate_shouldReceiveError() {
        //given
        final String expectedTitle = null;
        final var expectedDescription = """
                The Blair Witch Project (1999) follows three filmmakers who venture into the Maryland woods to 
                investigate the Blair Witch legend. As strange events unfold, paranoia and fear take over, 
                leaving them unsure if they’re being hunted by a supernatural force. 
                """;
        final var expectedLaunchedAt = Year.of(1999);
        final var expectedDuration = 78.5;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.AGE_18;
        final var expectedCategories = Set.of(CategoryID.generateUnique());
        final var expectedGenres = Set.of(GenreID.generateUnique());
        final var expectedCastMembers = Set.of(CastMemberID.generateUnique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be null";

        final var video = Video.create(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedPublished,
                expectedOpened,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );
        final var validator = new VideoValidator(video, new ThrowsValidationHandler());

        // when
        final var actualError = Assertions.assertThrows(DomainException.class, () -> validator.validate());

        // then
        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenEmptyTitle_whenCallingValidate_shouldReceiveError() {
        final var expectedTitle = "";
        final var expectedDescription = """
                The Blair Witch Project (1999) follows three filmmakers who venture into the Maryland woods to 
                investigate the Blair Witch legend. As strange events unfold, paranoia and fear take over, 
                leaving them unsure if they’re being hunted by a supernatural force. 
                """;
        final var expectedLaunchedAt = Year.of(1999);
        final var expectedDuration = 78.5;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.AGE_18;
        final var expectedCategories = Set.of(CategoryID.generateUnique());
        final var expectedGenres = Set.of(GenreID.generateUnique());
        final var expectedCastMembers = Set.of(CastMemberID.generateUnique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be empty";

        final var video = Video.create(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedPublished,
                expectedOpened,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );
        final var validator = new VideoValidator(video, new ThrowsValidationHandler());

        // when
        final var actualError = Assertions.assertThrows(DomainException.class, () -> validator.validate());

        // then
        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenTitleWithLengthGreaterThan255_whenCallingValidate_shouldReceiveError() {
        final String expectedTitle = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam at tempus nunc, sed aliquet sapien. 
                Donec pulvinar in neque vulputate rutrum. Donec a suscipit sem. Phasellus et dignissim ante. 
                Integer eget laoreet odio, id semper lorem. Aliquam eget libero.
                """;
        final var expectedDescription = """
                The Blair Witch Project (1999) follows three filmmakers who venture into the Maryland woods to 
                investigate the Blair Witch legend. As strange events unfold, paranoia and fear take over, 
                leaving them unsure if they’re being hunted by a supernatural force. 
                """;
        final var expectedLaunchedAt = Year.of(1999);
        final var expectedDuration = 78.5;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.AGE_18;
        final var expectedCategories = Set.of(CategoryID.generateUnique());
        final var expectedGenres = Set.of(GenreID.generateUnique());
        final var expectedCastMembers = Set.of(CastMemberID.generateUnique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should be between 1 and 255 characters";

        final var video = Video.create(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedPublished,
                expectedOpened,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );
        final var validator = new VideoValidator(video, new ThrowsValidationHandler());

        // when
        final var actualError = Assertions.assertThrows(DomainException.class, () -> validator.validate());

        // then
        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenEmptyDescription_whenCallingValidate_shouldReceiveError() {
        //given
        final String expectedTitle = "The Blair Witch Project";
        final var expectedDescription = "";
        final var expectedLaunchedAt = Year.of(1999);
        final var expectedDuration = 78.5;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.AGE_18;
        final var expectedCategories = Set.of(CategoryID.generateUnique());
        final var expectedGenres = Set.of(GenreID.generateUnique());
        final var expectedCastMembers = Set.of(CastMemberID.generateUnique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' should not be empty";

        final var video = Video.create(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedPublished,
                expectedOpened,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );
        final var validator = new VideoValidator(video, new ThrowsValidationHandler());

        // when
        final var actualError = Assertions.assertThrows(DomainException.class, () -> validator.validate());

        // then
        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    public void givenDescriptionWithLengthGreaterThan4000_whenCallingValidate_shouldReceiveError() {
        //given
        final String expectedTitle = "The Blair Witch Project";
        final var expectedDescription = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer turpis lorem, mattis non arcu nec, dapibus viverra lacus. Pellentesque aliquet purus non mauris ornare, eu porttitor lorem maximus. Donec aliquam laoreet quam id finibus. In luctus est non dignissim dictum. Suspendisse mattis leo malesuada, commodo justo et, tempus neque. Donec sollicitudin lacus eget dictum suscipit. Curabitur porttitor nisi eu diam venenatis, sed commodo tellus tincidunt. Vivamus consequat tortor et est rutrum, mollis eleifend velit mollis. Sed molestie id nibh vitae blandit. Ut arcu ipsum, auctor id convallis id, blandit et eros. Praesent nisi odio, mattis quis blandit vel, pulvinar vitae urna. Duis sed est tempus, molestie nulla quis, pulvinar ligula. In sit amet velit non ex dapibus pretium.
                
                Nam et lectus nibh. Phasellus posuere, tellus eu commodo finibus, eros leo vestibulum nibh, in ornare leo felis at ex. Nam nec fermentum metus. Suspendisse vitae eros dui. Praesent ultricies, urna ut posuere varius, libero est pharetra nisl, a cursus orci magna maximus dui. Suspendisse consequat sed risus scelerisque rhoncus. Donec pretium arcu velit, hendrerit aliquet sapien finibus ut. Suspendisse maximus sit amet augue eget viverra. Integer malesuada egestas felis. Duis feugiat nec nunc ut sollicitudin. Ut tempor convallis metus, non facilisis orci semper in. Sed a ligula ac libero dignissim iaculis vulputate ac metus.
                
                Quisque sed mattis eros. Nunc commodo ipsum imperdiet pellentesque eleifend. Nullam accumsan diam urna, a mattis justo elementum ac. Fusce vitae nunc et ex dictum laoreet. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Suspendisse non ultrices enim. Nullam ultrices, metus sit amet auctor scelerisque, ligula quam imperdiet est, eget consectetur lectus ligula id velit. Ut venenatis nec dui aliquet laoreet. Quisque consectetur urna vel blandit laoreet. Maecenas pretium commodo consequat. Donec ac accumsan lorem. Etiam sit amet iaculis elit, non sagittis ligula. Ut imperdiet magna vitae cursus tincidunt. Suspendisse gravida ex quis purus varius, eu mattis nisl cursus. Morbi in malesuada diam. Morbi vel cursus nulla.
                
                Maecenas vitae mauris pharetra, maximus mauris ut, aliquam turpis. Curabitur at consectetur quam. Nam condimentum finibus tellus non congue. Maecenas sollicitudin erat in risus tincidunt, at lobortis odio porta. Pellentesque varius, turpis nec vulputate euismod, risus leo tincidunt lectus, pulvinar accumsan ligula dui a lectus. Mauris lacinia tincidunt leo convallis finibus. Sed mattis velit in sem placerat, vitae sagittis purus blandit. Ut ac dignissim elit. Morbi nisi felis, fermentum id lorem ac, gravida sodales arcu. Suspendisse suscipit sed risus et maximus. Vivamus sodales hendrerit lectus ac vehicula. Vivamus vel vehicula leo. Pellentesque gravida nec ex quis finibus.
                
                Etiam at dui id nulla faucibus cursus. Phasellus pharetra ante eu dui rutrum porta. Nunc fermentum ligula eu dapibus scelerisque. Aenean auctor justo ipsum, vitae condimentum metus convallis at. Integer iaculis felis vitae rhoncus venenatis. Sed sodales sollicitudin lacus, sit amet imperdiet risus elementum in. Cras sed euismod nisl. Aliquam a tellus dapibus, cursus orci et, semper ex. Vivamus eget dictum libero. Curabitur velit nisi, fermentum vel arcu eu, placerat lacinia justo. Ut ex nisi, ornare in tortor non, egestas pharetra nunc. Integer at viverra augue, sed laoreet elit. Aenean viverra eleifend purus, in commodo dolor dapibus ac.
                
                Suspendisse ut aliquet magna, a interdum tellus. Cras rutrum, urna eu hendrerit rhoncus, felis tortor porttitor augue, sed vehicula odio elit vel dolor. Pellentesque sagittis ex ut erat facilisis mattis. Duis lacinia dolor a ligula tempus, quis accumsan purus faucibus. Integer pharetra lacus sed lobortis efficitur. Aliquam iaculis urna sed augue tempor bibendum. Donec imperdiet, lorem sit amet ultricies luctus, tellus mauris auctor felis, id vulputate mauris ex sit amet sapien. Cras vel augue id lorem rutrum laoreet a tempus metus. Phasellus nec mauris placerat, consequat augue ac, dapibus augue. Interdum et malesuada fames ac ante ipsum primis in faucibus.
                
                Curabitur aliquam quis turpis quis convallis. Sed tincidunt, lacus et condimentum pulvinar, massa lorem luctus nisi, ut maximus sapien magna in neque. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Suspendisse fringilla, augue sed vulputate varius, enim felis cursus felis, vitae elementum sem elit sit amet lectus. Nulla dignissim libero id orci tincidunt, vitae efficitur ex pulvinar. Fusce dictum hendrerit sapien. Integer leo purus, convallis vel efficitur nec, dictum ac mi. Phasellus sit amet malesuada quam. Mauris pretium hendrerit aliquet. Ut lobortis, quam quis ornare bibendum, mauris risus ultrices justo, ac vestibulum purus est eu ante. Curabitur dignissim, velit eu vestibulum ultricies, sapien mi porta dolor, hendrerit maximus orci augue et enim. Nunc accumsan sollicitudin orci quis mollis. Proin eget magna at libero venenatis pretium quis sed leo. Vestibulum elementum venenatis lorem, ut placerat nibh molestie eu. Nam ultrices semper erat ut tincidunt.
                
                Mauris pretium odio non odio convallis dignissim. Cras metus enim, tincidunt sit amet justo ac, condimentum accumsan sem. Aliquam in libero a ante accumsan dignissim ut ac augue. Vestibulum dignissim, eros ut pretium hendrerit, nisi erat pulvinar velit, nec maximus felis orci eu libero. Suspendisse porta sem eu tristique fermentum. Quisque convallis odio vel felis vulputate pretium. Nam commodo libero at dolor placerat egestas. Suspendisse sodales, ex vitae fermentum bibendum, purus leo auctor dui, non pulvinar nisi orci nec dui. Donec at ex quis libero interdum sagittis. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Fusce dapibus id justo ac pretium. Cras ut quam sed augue tempor auctor. Nam vestibulum massa augue, id accumsan leo sodales ut.
                
                Vestibulum ultricies orci feugiat nibh pulvinar, quis iaculis augue malesuada. Aliquam malesuada ultricies urna. Ut efficitur tempus mattis. Duis sit amet vestibulum massa. Nulla hendrerit varius metus consectetur convallis. Sed dictum ante eros, ut dapibus diam mattis sed. Curabitur posuere at velit ut pharetra. Donec ipsum arcu, interdum sit amet lacus at, dignissim lobortis nulla. Morbi sit amet mollis mauris, id tempor tortor. Maecenas hendrerit metus ut magna congue, eu consectetur est mattis. Morbi at blandit augue. Etiam mollis sit amet dui non maximus. In vel ipsum non risus luctus fermentum at sed nisi.
                
                Vestibulum varius dictum nibh, vitae laoreet metus sollicitudin sit amet. Morbi finibus leo quis consequat egestas. Etiam ipsum lectus, dignissim eu cursus eget, maximus sed lacus. Phasellus dui diam, commodo sit amet urna in, consectetur accumsan mauris. Praesent sem sem, tincidunt id lobortis nec, sollicitudin et arcu. Sed nec nisi sit amet odio convallis molestie eget nec mi. Nunc at elit pretium, varius urna et, venenatis nibh. Nunc fringilla sit amet lacus eget pulvinar. Cras sit amet enim mattis, facilisis tellus quis, ultricies massa. Sed ac suscipit justo, non blandit metus. Donec maximus tellus ac dictum iaculis. In pretium metus in libero consectetur lobortis. Nullam non lectus id metus vulputate sagittis nec ut dui. Nulla suscipit felis a tempor tincidunt. In et vehicula nunc. Donec feugiat mattis leo sit amet laoreet.
                
                Etiam egestas leo lorem, non placerat nibh volutpat in. Maecenas in neque id mi consequat fermentum. Maecenas in neque est. Cras pretium metus vitae mauris egestas fringilla. In ac nisi et odio feugiat tincidunt sed facilisis magna. Aliquam ultricies sem nec nisl aliquam tempus. Cras est magna, porta vitae venenatis vel, facilisis feugiat quam. Mauris id quam feugiat, viverra orci quis, auctor enim. In feugiat fringilla augue, sit amet eleifend mi faucibus a. Donec id pretium turpis. Nam congue sodales enim, ut imperdiet diam. Etiam ullamcorper ultrices nibh vel porttitor.
                
                Curabitur vitae viverra erat. Nullam mauris quam, tempor a massa in, ultricies efficitur urna. Cras aliquam velit in hendrerit pharetra. Vivamus molestie, ligula eu aliquet venenatis, sem purus dapibus augue, non cursus nibh nibh id enim. Phasellus elementum rutrum nisi, vitae fermentum mi fringilla eget. Mauris in ipsum tempor, blandit justo at, bibendum orci. Suspendisse tincidunt nunc sit amet sem malesuada, sed volutpat tellus vulputate. Fusce iaculis scelerisque dolor, vitae hendrerit erat iaculis vitae. Nullam ultricies mauris nec velit feugiat, at congue odio sodales. Praesent nec facilisis tortor. Phasellus enim ligula, venenatis eu semper ut, egestas sit amet velit. Nunc sit amet porttitor felis. Nullam eget rutrum turpis. Fusce pretium tempus lectus in viverra. Phasellus accumsan eleifend purus ut laoreet. Ut scelerisque ligula ac erat imperdiet, quis molestie nisl feugiat.
                
                Maecenas eget dignissim metus. Nunc finibus velit vitae sapien maximus semper. Aliquam iaculis mauris sit amet lacinia tempus. Sed est tellus, suscipit eget rhoncus eget, sollicitudin eget sem. Praesent lacinia arcu eget ultricies rhoncus. Phasellus nec est id mauris pharetra euismod. Donec blandit eros id dictum elementum. Proin pulvinar risus nec posuere fringilla. Donec eget tortor rhoncus diam pellentesque tincidunt sit amet luctus purus. Duis placerat auctor nibh nec tempor. Vestibulum elementum mollis nisl ut viverra. Integer tempus augue nec tellus viverra, nec lacinia est posuere. Quisque ac sapien ut dolor iaculis porta.
                
                Donec felis justo, vulputate volutpat vulputate id, vehicula sit amet nunc. Nullam egestas nulla velit, vel gravida nisi ornare in. Duis nec arcu ac dui cursus eleifend. Curabitur ultricies imperdiet pellentesque. Cras faucibus nunc sit amet sapien aliquam eleifend. Quisque elementum volutpat sagittis. Integer imperdiet congue sem. Mauris mattis vitae lorem id mattis. Duis lobortis auctor commodo. Nam venenatis neque eget risus elementum, ut tempus metus commodo. Vestibulum eu malesuada ligula. Pellentesque sed nisl quis est blandit maximus vitae gravida nulla. Aliquam dictum sollicitudin est, nec posuere lorem dictum sit amet. Praesent id tellus finibus diam pellentesque hendrerit eu sit amet nibh. Nullam erat lorem, sollicitudin sed turpis ut, tincidunt bibendum quam. Donec ut sagittis ligula, at porta nunc.
                
                Vivamus eleifend pretium ligula. Proin eget tempus ligula. Nunc a mattis quam. In ut elit nec enim dignissim eleifend non sit amet est. Nunc iaculis libero sit amet odio blandit vestibulum. Vivamus nibh elit, sollicitudin a ornare eget, suscipit in leo. Proin sed blandit mauris. Integer semper tortor ut justo sodales scelerisque. Pellentesque lacinia neque velit, eget porttitor lorem fermentum at.
                
                Duis porttitor neque sed tempor tempus. Proin ac nunc quam. Integer sed tellus id elit convallis auctor. Praesent iaculis scelerisque sem, vel luctus augue porta quis. Mauris euismod metus eget nunc tempor consequat. Phasellus vel lectus viverra, hendrerit est sit amet, varius dui. Vivamus suscipit dictum placerat. Fusce malesuada suscipit suscipit. Aliquam ut nunc tempus, venenatis diam eu, rhoncus risus. Fusce lacinia eros et sagittis lobortis. Donec varius ultrices finibus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Donec quis est magna.
                
                Morbi id lacus eu purus consectetur aliquet ac ac sem. Nullam sed condimentum urna, eget hendrerit dui. Maecenas condimentum facilisis orci, vitae pretium massa lacinia in. Cras non nisi ut mauris pellentesque porta posuere eget lorem. Pellentesque imperdiet felis eget leo posuere, fermentum maximus sem gravida. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Donec varius mauris vehicula, tristique urna a, aliquet erat. Suspendisse quis nisi sit amet neque consectetur condimentum. Sed vel mauris imperdiet, tristique neque at, auctor leo. Aliquam nisi eros, laoreet ac luctus vitae, tempus ac tortor. Proin in ante convallis, iaculis quam vel, pretium quam. Suspendisse euismod arcu in erat venenatis, eget accumsan urna pharetra. Ut risus leo, consequat laoreet purus pharetra, faucibus tempor magna. Nullam condimentum in neque at fringilla.
                
                Pellentesque in suscipit erat. Quisque ac mauris eros. Donec blandit viverra ultricies. Morbi sit amet purus semper, placerat sapien ut, sollicitudin libero. Pellentesque ac dui ac purus dictum semper. Vivamus quis tincidunt dui. Praesent est tellus, faucibus eu sagittis ut, lacinia at magna. Nunc iaculis maximus purus, nec imperdiet quam laoreet nec. Phasellus id lorem lacinia, aliquet tortor a, interdum arcu. Nam at arcu nec dolor posuere scelerisque.
                
                Vivamus non aliquam ex. Nullam aliquet diam et nisi iaculis ultricies. Fusce vel tempor turpis, in aliquet ligula. Nunc maximus, arcu et vehicula imperdiet, augue ligula vulputate tellus, at hendrerit libero dolor auctor odio. Donec leo ligula, sagittis eu placerat eget, ultricies in eros. Donec congue nec diam sed blandit. Cras imperdiet dapibus laoreet. Donec non dignissim ante. Nulla massa velit, maximus hendrerit odio nec, luctus congue massa. Duis vehicula velit vitae diam sagittis, posuere efficitur velit pellentesque. Pellentesque mauris mauris, dictum eget ex ut, placerat eleifend urna. Morbi metus ligula, aliquet a malesuada nec, luctus non lorem. In quis ante volutpat, finibus augue vel, scelerisque mi.
                
                In gravida eros sit amet pretium vestibulum. Nullam rhoncus mauris imperdiet, aliquam magna vitae, vestibulum elit. Praesent sed sollicitudin orci. Suspendisse at vulputate erat. Donec quis augue quis ligula volutpat efficitur vel vitae ex. Ut quis volutpat diam, in cursus magna. Sed sit amet sapien vulputate, consequat nisl vel, vehicula magna. Sed euismod hendrerit nisl a vulputate. Quisque eu purus ligula. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Interdum et malesuada fames ac ante ipsum primis in faucibus. Etiam blandit tincidunt diam at varius. Ut hendrerit ligula enim, ut accumsan mauris sodales ultrices. Curabitur semper mi id lorem aliquet, sit amet fermentum nibh vulputate. Curabitur sit amet ornare urna, vitae commodo erat. Maecenas luctus volutpat velit, eu semper neque venenatis aliquet.
                
                Sed arcu tellus, feugiat id euismod non, vulputate vel lorem. Aliquam erat volutpat. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Etiam dictum vitae tortor faucibus laoreet. Cras eget sapien ornare, tincidunt massa sit amet, aliquam neque. Ut volutpat erat eros, blandit consequat sapien semper rutrum. Aenean lacinia, elit gravida blandit pharetra, ante tellus suscipit dui, ut vehicula felis dui nec est. Cras hendrerit vulputate lorem id hendrerit. Donec convallis, mauris id euismod tristique, nibh velit lobortis est, eget dictum nisl mi ut arcu. Integer cursus massa eget rhoncus dignissim. Maecenas tincidunt metus in massa dignissim faucibus. Fusce ac orci aliquet, aliquet sapien sed, sagittis libero. Etiam at nisi diam. Curabitur tincidunt, orci nec dignissim fermentum, libero magna aliquet ante, nec congue ligula dui sed neque. Maecenas iaculis metus placerat eleifend molestie.
                
                Morbi vehicula efficitur consequat. Morbi congue risus diam, id euismod justo sodales vitae. Vestibulum sit amet augue mi. Nunc ipsum neque, laoreet id nunc vitae, tristique sagittis lorem. Pellentesque malesuada purus nec posuere egestas. Nunc vel vehicula libero. Quisque blandit nisl vel sapien scelerisque egestas. Pellentesque nec iaculis nisi.
                
                Sed elit felis, elementum id lorem non, condimentum maximus mi. Aenean non odio ac purus faucibus finibus. In hac habitasse platea dictumst. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Donec tincidunt lorem ut lectus sollicitudin commodo. Duis accumsan enim quis justo viverra lacinia. Quisque non molestie ligula. Praesent tincidunt nulla a velit dapibus, in tincidunt nulla placerat. In vel libero at orci consectetur ultrices vitae eu tellus. Donec id tellus justo. In tellus dolor, tristique eu dui sed, sagittis lobortis urna. Phasellus sodales mauris eu nulla lobortis, id consequat turpis euismod. Aliquam nisi purus, mollis ut sem eget, sollicitudin pulvinar nulla. Nam a sapien a augue finibus molestie.
                
                Proin enim risus, ornare at urna id, pulvinar malesuada nunc. Ut consequat nulla sit amet tincidunt eleifend. Fusce metus tortor, bibendum id consequat a, auctor in mauris. Nulla iaculis bibendum quam, quis ultrices magna auctor nec. Cras id ipsum mi. Sed id venenatis orci, quis volutpat nibh. Fusce lacinia magna eros, sit amet viverra sapien tincidunt eu.
                
                Ut fermentum venenatis lacus et volutpat. Praesent in enim pretium, lacinia nisl quis, pulvinar odio. Sed sollicitudin bibendum hendrerit. Quisque ullamcorper blandit consectetur. Etiam congue felis id justo sollicitudin, sit amet rutrum purus ullamcorper. Praesent blandit condimentum ipsum, a convallis orci ornare efficitur. Quisque ut augue sed quam dapibus tristique eget ac est. Quisque rutrum enim id faucibus ultricies. Vivamus tempus lectus in metus volutpat ullamcorper. Vivamus cursus lacinia auctor. Proin vitae nulla a libero ultrices tincidunt.
                
                Nullam in sapien maximus, interdum sem eget, maximus tellus. Etiam eget tortor pharetra risus laoreet placerat. Integer tempor leo mi, non convallis metus commodo ac. Aenean vehicula aliquam erat. Curabitur sit amet quam a sapien varius hendrerit et lacinia arcu. Donec fermentum, quam vitae maximus pharetra, orci orci congue est, vel lobortis turpis quam quis sem. Nulla vel nisl at erat tincidunt vehicula. Donec id mattis erat. In eget suscipit turpis, facilisis cursus massa. Vivamus malesuada feugiat libero in viverra. Ut porttitor fermentum ex, id tincidunt tellus consequat vel. Pellentesque non lectus sed purus vestibulum posuere sed at tellus. Mauris blandit tortor ut nunc rutrum bibendum. Proin nisl libero, ultricies sit amet cursus sit amet, rutrum at mauris. Nam bibendum facilisis aliquam. Praesent mollis rhoncus tempor.
                
                Quisque sit amet pharetra metus, vel molestie eros. Duis dui lectus, posuere ut mollis ac, efficitur at dolor. Vestibulum ipsum quam, ullamcorper fringilla egestas nec, porta sed est. Cras euismod at ex et egestas. Praesent iaculis luctus elementum. Donec quis est magna. Curabitur nisl erat, cursus eu vulputate id, dictum ac orci. Donec at velit in neque rhoncus tincidunt sed et leo.
                
                Fusce vestibulum nec justo eget venenatis. Nunc ornare ullamcorper pharetra. Donec nec lorem nunc. Nulla tortor enim, eleifend sit amet erat in, laoreet imperdiet ex. Sed nisi nisi, iaculis porttitor faucibus in, sollicitudin ut ex. Curabitur eu ipsum ut nisi finibus pellentesque in id nisi. Nam diam odio, ullamcorper id erat vel, dictum scelerisque sapien. Morbi lobortis erat ut nibh maximus ultrices. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Mauris leo lorem, elementum id gravida at, placerat et mauris. Maecenas vehicula massa eu ligula consequat, fermentum cursus nisl lacinia. Duis rutrum vulputate metus, eu condimentum neque congue non. Donec ac libero quis ligula pharetra mattis eget et turpis. Ut at est nec est facilisis lobortis.
                
                Phasellus pellentesque gravida est, et tincidunt velit mollis a. Fusce neque enim, rhoncus nec lobortis et, suscipit non urna. Aenean iaculis elit et dui egestas vulputate. Proin sollicitudin in urna eu lobortis. Maecenas ornare hendrerit lorem non vehicula. Vestibulum porttitor feugiat est vel luctus. Nam eu ligula vestibulum, condimentum justo nec, finibus nisi. Vivamus sed dolor non dui aliquam tincidunt sed non metus. Vestibulum semper, ligula in congue blandit, ligula ex euismod ex, sed ultrices orci libero ut justo. Mauris dolor neque, tempor non pellentesque venenatis, vehicula eget leo. Pellentesque vehicula in eros id suscipit. Nulla facilisi. Vestibulum posuere libero quam, eleifend vestibulum velit mattis a. Mauris tincidunt fringilla erat, sit amet sodales erat blandit vitae. Duis nec mi lorem. Duis in elit erat.
                
                Aliquam nulla erat, malesuada ut efficitur eu, iaculis sit amet libero. Suspendisse in egestas nisl. Praesent egestas ex est, vitae efficitur felis interdum eu. Vivamus malesuada enim eget nulla convallis, at malesuada lorem suscipit. Vivamus quis felis at diam commodo iaculis. Vivamus et nibh nec neque egestas ultrices. Phasellus orci mauris, volutpat id tortor a, convallis finibus quam. Duis facilisis, justo at elementum consectetur, velit lectus egestas mauris, eget tincidunt quam eros ac felis. Curabitur finibus blandit orci sit amet sollicitudin. Morbi et varius erat. Cras mattis dolor vel nisi consequat hendrerit. In ut dictum dui. Maecenas semper consequat tellus at mattis. Fusce consectetur ut lorem ut varius. Morbi volutpat elit a neque iaculis, ac mattis metus imperdiet.
                
                Aliquam risus nibh, facilisis quis hendrerit sed, pellentesque sit amet velit. Morbi sem libero, vehicula et hendrerit eget, vulputate sit amet lectus. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; In bibendum lacus id diam scelerisque convallis. Nullam in augue eget turpis euismod eleifend. Curabitur vehicula, dui sed finibus malesuada, mauris metus porttitor nisi, et sagittis justo erat sit amet mi. Sed dui ipsum, laoreet eu sollicitudin at, imperdiet at nisl. Mauris sit amet purus sit amet lectus ullamcorper feugiat sed eget nisl. Integer euismod tincidunt malesuada. Nunc aliquam vestibulum orci, vitae tempor libero porttitor vel. Aenean ac eros tempor, pharetra ante et, egestas ante. Suspendisse ullamcorper leo arcu, vitae mollis nibh vulputate ut. Phasellus augue neque, cursus ut dignissim id, mattis eget lorem. Etiam ultricies mauris tellus, dictum efficitur erat tempus in. Mauris tempus convallis metus ullamcorper dapibus. Sed sit amet nibh libero.
                
                Cras elit ante, consectetur vel turpis sed, ultrices scelerisque nunc. Fusce vel lorem laoreet, ultricies nibh et, sodales justo. Ut sed sem at augue aliquam molestie. Vivamus id maximus nibh. Pellentesque eget sollicitudin enim, non feugiat quam. In convallis in diam hendrerit feugiat. Vivamus sodales lectus tempus ligula suscipit bibendum. Sed condimentum convallis nulla, vel pharetra elit egestas id. In sed placerat arcu. Duis dictum, massa a consectetur eleifend, ipsum elit ultrices lectus, ac vestibulum magna lacus vitae massa. Suspendisse vestibulum, velit eu porta iaculis, neque tellus efficitur purus, id mollis dolor turpis id felis. Etiam tristique diam lorem, a accumsan ex congue vitae. Sed tempor risus massa, sit amet feugiat mauris commodo nec. Mauris eu dui a erat facilisis scelerisque. Sed convallis porta ante, ut congue erat hendrerit vitae.
                
                Phasellus nec cursus nulla, non tempor erat. Phasellus vel facilisis dolor. Donec tempus vulputate enim, eget bibendum libero bibendum tristique. Mauris nec lectus auctor, auctor libero eget, hendrerit velit. Fusce condimentum accumsan velit ut aliquet. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Vestibulum posuere imperdiet pharetra. Ut et lorem orci. Sed dapibus luctus hendrerit. Nam vel quam libero. Aliquam sem lacus, euismod ac lorem ut, pretium ultrices velit.
                
                Curabitur egestas elit sit amet nunc fringilla tempor. Morbi aliquam finibus lorem. Curabitur eleifend et nibh ut mollis. Donec egestas ac nisi nec lacinia. Aliquam vitae consequat ligula. Integer imperdiet lobortis accumsan. Pellentesque dictum feugiat ligula ut faucibus. Maecenas vulputate tristique mauris, id semper orci ultrices eu. Quisque rutrum nisi lacus, vel congue libero sagittis lacinia. Nam id venenatis justo. Cras eget bibendum diam. Sed in consequat arcu. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                
                Donec id accumsan arcu. Sed suscipit at elit id rutrum. Fusce auctor vel mauris vel dictum. Etiam leo ante, sagittis vitae neque non, rutrum facilisis ipsum. Praesent id feugiat augue. Etiam rutrum tortor nec mauris molestie, eu rutrum risus maximus. Ut ipsum nisi, varius quis nulla eget, tempus gravida velit. Donec eu consequat neque. Duis ultrices nibh felis, vehicula pharetra velit pellentesque lobortis.
                
                Duis neque ex, lobortis a bibendum vestibulum, lacinia quis justo. Curabitur justo nisl, feugiat in libero vel, cursus luctus mauris. Morbi rutrum sapien id tortor ornare, et tempor libero maximus. Praesent in ante non nulla hendrerit vestibulum sit amet ut mi. Curabitur lacus leo, venenatis non est in, mollis pulvinar dui. Integer a facilisis diam. Vestibulum sed efficitur mi. Aenean ultricies ligula scelerisque hendrerit congue. Pellentesque sed ante sit amet enim consequat lacinia. Integer cursus facilisis fringilla. Sed risus lacus, vehicula id lorem eleifend, ullamcorper consequat massa.
                
                Cras eu accumsan ligula. Phasellus quis tellus ut erat sagittis pretium. Proin non elementum erat. Sed vel tempor dui, eu feugiat nulla. Mauris erat risus, sagittis sed turpis vitae, faucibus hendrerit tortor. Sed scelerisque sollicitudin placerat. Nulla mollis sed dui eu varius.
                
                Sed finibus sapien lacus, et euismod nibh dignissim at. Etiam eleifend luctus ipsum a sollicitudin. Praesent vestibulum mi in velit vehicula tincidunt. Maecenas fermentum nisi tellus, a laoreet orci pellentesque id. Proin gravida urna eros, quis pharetra diam elementum a. Mauris lectus dui, tempus vitae tellus id, mollis sollicitudin tellus. Pellentesque sed ultricies leo. Ut sagittis dictum finibus. Nunc commodo varius est tincidunt volutpat. Sed facilisis lectus in urna malesuada, rhoncus scelerisque felis iaculis.
                
                Vivamus lobortis tristique augue. Duis ligula mauris, volutpat sed lectus non, tempus molestie nulla. Curabitur sit amet auctor enim. Nunc in leo sit amet nulla vulputate sagittis. Curabitur lorem metus, pharetra vitae malesuada sed, finibus id lacus. Ut ipsum odio, egestas vel tortor id, tempus commodo ligula. Morbi mollis felis imperdiet suscipit porta. Curabitur pretium sapien ut velit rhoncus, sagittis varius lectus porta. Cras et ante sit amet mi facilisis sodales. Quisque at mauris condimentum, volutpat nunc eu, condimentum lorem. Praesent tincidunt pellentesque finibus. Etiam eleifend ac elit vel imperdiet.
                
                Donec ultricies facilisis augue, id iaculis dolor. Pellentesque sagittis urna non tellus consequat dapibus. In hac habitasse platea dictumst. Nullam dictum purus quis sapien luctus, et consequat neque elementum. Interdum et malesuada fames ac ante ipsum primis in faucibus. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Proin id blandit orci. Nunc et congue leo, eget sagittis ligula. Mauris vel ultrices libero. Mauris risus justo, dictum et neque rhoncus, gravida commodo ipsum. Maecenas dignissim ipsum id enim sagittis, et luctus felis pretium.
                
                Proin sit amet dui accumsan, tempus dolor sed, ornare purus. Donec ac tempor sapien. Aliquam pretium luctus nisi eget faucibus. Duis ac tellus non felis consectetur commodo a sit amet mi. Integer vel felis turpis. Integer sodales metus sed finibus rutrum. Sed gravida accumsan justo sit amet auctor. Nulla pellentesque nisi in metus dignissim, eget gravida nulla gravida.
                
                Fusce porttitor vestibulum viverra. Pellentesque elementum ornare vehicula. Nam imperdiet blandit neque. Sed sapien ligula, ultrices eget nisl eu, consequat varius justo. Mauris sed augue ac eros elementum lacinia. In porta magna nec tortor placerat cursus. Quisque fringilla felis et nibh scelerisque convallis. Mauris elementum, leo vel volutpat imperdiet, arcu dui vestibulum orci, at convallis quam felis sit amet velit. Nunc vulputate et massa a aliquam. Fusce mattis turpis magna, at aliquet turpis laoreet vel. Aliquam lacinia dui ullamcorper mauris finibus, quis sagittis sapien aliquam. Integer id nunc sed arcu condimentum sodales a in metus. Vestibulum non est ut enim laoreet bibendum suscipit ac dui. Phasellus sit amet mollis metus. Pellentesque congue tellus ut fringilla rutrum.
                
                Quisque purus nisl, egestas ac pretium eget, fermentum nec elit. Phasellus quam nisi, semper vel rhoncus ac, mollis ultrices diam. Nulla nec massa id turpis condimentum iaculis. Praesent rhoncus lacinia consequat. Praesent pellentesque scelerisque lorem, ut aliquam est fermentum eleifend. Fusce consequat nulla ut enim porta, quis euismod velit sollicitudin. In ac turpis massa. Donec sollicitudin porttitor tellus in iaculis. Maecenas lacinia, erat at iaculis gravida, enim neque gravida neque, sed ultricies nulla augue a est. Aenean interdum eros eu dui interdum, a gravida dolor mattis. Curabitur ut dolor mattis, convallis libero vel, volutpat dolor. Vestibulum vulputate felis in ipsum varius dignissim. Aliquam erat volutpat.
                
                Integer auctor turpis nec augue rhoncus, id cursus ipsum dignissim. Morbi dui felis, pharetra nec augue non, condimentum lacinia tellus. Nunc aliquam iaculis arcu, eget lobortis turpis aliquet vel. Maecenas vulputate urna id urna lobortis vehicula sit amet non est. Integer in maximus nunc. Sed sed purus eget mauris tincidunt dapibus. Fusce venenatis ut urna quis laoreet. Fusce dui leo, vehicula ut suscipit ut, blandit ut mi. Nunc eget tempus massa. Mauris fermentum nisi non vulputate tincidunt. Sed erat dui, dictum non erat ac, posuere condimentum metus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Pellentesque rutrum, libero et venenatis iaculis, nisi leo sagittis erat, sit amet accumsan urna enim a dolor. Maecenas ac consequat erat. Morbi a orci placerat, dictum risus tincidunt, sollicitudin nulla. Nam quis est a leo ullamcorper ullamcorper sit amet non mi.
                
                Etiam interdum libero risus, eu vestibulum enim commodo sit amet. Maecenas posuere, urna eleifend fermentum suscipit, ipsum magna accumsan felis, rhoncus viverra purus magna ac elit. Nulla at magna ex. Vestibulum egestas, lacus sed efficitur congue, sapien velit feugiat ligula, quis imperdiet sem risus vitae ligula. Mauris mollis, turpis in porttitor tristique, est ex tincidunt libero, id auctor felis metus non erat. Ut porta nibh at nibh fringilla tincidunt eget sit amet diam. Praesent consequat enim eu erat aliquam, quis finibus nisl tincidunt. Donec vulputate, nisi ac consectetur lobortis, lacus arcu tempor mauris, ut aliquet ligula metus eget ante. Nulla vitae erat ullamcorper, lobortis lorem non, mattis velit. In molestie ornare felis eget blandit. Sed venenatis lacus at dolor imperdiet, quis tempus mauris accumsan. Vestibulum vitae lectus non tortor venenatis consectetur. Sed libero est, tempus et diam egestas, pulvinar commodo nunc. Cras id purus eros. Proin placerat dolor sit amet dictum finibus. Duis ut lorem ipsum.
                
                Nunc nec justo quis ante fermentum dignissim quis id lorem. Duis ut tellus fermentum, condimentum ex at, malesuada leo. In ultricies a urna sit amet hendrerit. Nullam pretium vel tellus vitae elementum. Phasellus fringilla lorem velit, et ornare magna tempus suscipit. Donec ullamcorper purus nec leo dictum consectetur. Cras porta euismod dolor vitae dignissim. Integer vitae est nec elit aliquet hendrerit. Donec sem risus, commodo et congue id, vulputate laoreet augue. Duis bibendum sapien eget ante vulputate ullamcorper. Aliquam malesuada ac urna nec maximus. Aenean at accumsan turpis. Sed volutpat commodo ipsum ac fringilla. Phasellus eget urna at arcu dictum scelerisque. Vivamus lacinia sollicitudin arcu, sit amet porta elit hendrerit nec.
                
                Aenean nunc nisl, ultrices sit amet porttitor vel, blandit non ligula. Nam vitae nisl quam. Nulla congue malesuada elit non venenatis. Vivamus et augue a justo porta aliquet quis non diam. Mauris sodales lacinia diam et faucibus. Suspendisse malesuada congue ipsum, et ullamcorper lorem sodales quis. Etiam id laoreet augue, et blandit lacus. Suspendisse consectetur augue nec sapien gravida, et tempus metus vulputate. In vel dui eu ligula posuere elementum quis fermentum ipsum. Aliquam erat volutpat.
                
                Maecenas tellus mi, sollicitudin ornare viverra sit amet, placerat eget felis. Nulla nunc dui, interdum non tempor sed, gravida non quam. Cras nec ex fringilla, aliquam nisi a, tempor augue. Morbi convallis blandit libero sit amet elementum. Donec lobortis pellentesque eros, quis euismod mi faucibus et. Ut luctus risus in venenatis imperdiet. Vestibulum eget quam nec ante elementum tempus nec id massa. Duis scelerisque posuere porta. Etiam luctus purus eget nibh hendrerit, at ultricies nisi tempor.
                
                Phasellus aliquet erat mauris, eget pellentesque ipsum rutrum ac. Nunc vel varius sapien. Etiam sodales, tortor vel vehicula euismod, risus turpis imperdiet augue, et semper nisl mauris ac ipsum. In lacus sem, varius eget consequat vel, molestie ac arcu. Fusce ornare congue pretium. Nullam fermentum quam sed finibus semper. Morbi porttitor nulla non orci ornare, a molestie turpis venenatis. Nulla placerat, ante ut scelerisque varius, ex diam elementum nibh, rhoncus faucibus dolor erat vitae nulla. Proin suscipit velit vitae nibh sagittis, non bibendum arcu cursus. Nulla ac ante egestas, tristique tortor non, suscipit eros. Proin mi ex, dignissim sit amet nulla a, laoreet commodo ante. Maecenas iaculis, dolor eu semper egestas, nibh lectus viverra metus, at molestie ipsum metus eu diam.
                
                Pellentesque id faucibus ligula, in volutpat turpis. Donec lacus lectus, efficitur sed vehicula vitae, eleifend eget lacus. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Nulla enim elit, consectetur interdum risus mollis, posuere fermentum lorem. Proin vestibulum dictum erat at placerat. Nunc ornare hendrerit velit, in semper quam. Etiam ullamcorper auctor blandit. Cras sed lacus in lorem volutpat laoreet. Morbi tincidunt libero vel metus dapibus hendrerit. Integer vitae dignissim ante. Integer gravida egestas lacus, non pulvinar neque laoreet ac.
                
                In eu cursus sapien. Mauris sodales mauris magna, nec sodales augue tempus vel. Maecenas faucibus elementum cursus. Duis ut lacinia ex. Nunc at semper turpis. Phasellus pharetra sit amet justo posuere cursus. Nullam eleifend tincidunt sem non convallis. Cras sed quam vitae urna vulputate ultrices.
                
                Morbi condimentum neque metus, non pharetra odio lacinia ac. Donec mollis lorem at cursus laoreet. Duis euismod nunc a arcu elementum, ac tincidunt risus aliquam. Nullam eget mauris erat. Suspendisse sed lacus sed velit auctor volutpat ac in sem. Donec vitae libero tincidunt, consectetur nunc in, accumsan urna. Donec sapien orci, luctus nec ipsum quis, malesuada fermentum velit. Donec efficitur tempus arcu, non hendrerit magna efficitur non. Vivamus non sapien a ante viverra finibus. Sed eget tellus ultrices augue ornare eleifend ut et est. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Curabitur vel consectetur mi. Nunc quis interdum mauris.
                
                Maecenas gravida, sem ac varius placerat, enim quam auctor augue, id mattis risus lorem a orci. Nunc gravida, nulla et sodales mollis, nulla risus elementum sem, sit amet volutpat risus nisi sit amet augue. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Morbi eu nunc eu sapien imperdiet suscipit. Fusce dui erat, varius ac dictum in, volutpat pretium augue. Vestibulum rhoncus, leo eu dignissim elementum, nunc ligula commodo ipsum, sed bibendum dui erat ut tortor. Nullam ac porttitor metus. Etiam congue, metus vitae auctor laoreet, risus est placerat nunc, sed congue augue est non libero. Nullam facilisis sem viverra nibh tempus, sed feugiat felis egestas. Maecenas eget purus consequat, cursus nisi in, semper magna.
                
                Etiam vitae elementum nibh, sit amet dignissim eros. Morbi vel dui justo. Donec varius lorem sit amet scelerisque dapibus. Aenean in odio bibendum, porta augue vel, pellentesque nisi. Sed iaculis odio nec feugiat commodo. Vivamus lobortis quam id diam faucibus, vel ultrices tellus tincidunt. Aenean rhoncus felis vitae mi mattis, id fringilla velit volutpat. Vestibulum egestas non justo eget dapibus. Integer sagittis elementum pulvinar.
                
                Nam auctor leo odio, in suscipit sem ultricies vitae. Nullam varius ipsum in luctus fringilla. Phasellus elementum sem turpis, quis auctor massa convallis at. Nunc molestie rutrum ex, in rutrum nibh venenatis hendrerit. Cras sodales bibendum dapibus. Integer aliquam neque nulla, quis suscipit lorem malesuada nec. Phasellus pulvinar dui sed purus dapibus, in lacinia odio pretium. Pellentesque vel dolor fermentum diam dapibus dictum. Ut ante tortor, consectetur consectetur laoreet sit amet, laoreet at lorem.
                
                Maecenas dui ante, accumsan sed dapibus id, condimentum tincidunt metus. Morbi placerat enim id sem bibendum, ut pharetra dui dignissim. Donec sodales aliquet ligula, sit amet ultrices urna aliquet eget. Etiam vestibulum iaculis placerat. Pellentesque rhoncus, nisl sit amet ornare hendrerit, quam mi elementum lorem, non venenatis libero mauris ac metus. In bibendum libero dictum odio lobortis tempus. Nam ultricies neque et auctor scelerisque. Duis vel nulla tempus, pellentesque sem ultrices, congue libero. Sed ex tortor, imperdiet eget ultricies eget, ullamcorper sed quam. Integer fermentum ornare accumsan. Aliquam aliquet mollis felis, nec luctus dolor tincidunt at. Aliquam non dictum lacus, mattis lacinia urna.
                
                Aenean at blandit turpis. Sed quis turpis posuere, fringilla arcu in, venenatis purus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Morbi varius vulputate libero, a congue nulla lobortis vel. Quisque placerat, velit et aliquam semper, lacus ligula gravida sapien, eu aliquet sem nulla id nunc. Donec non ex quam. Vivamus a consectetur metus, non tempor neque. In hac habitasse platea dictumst. Fusce eu neque sagittis, hendrerit turpis a, scelerisque elit.
                
                Suspendisse id gravida neque. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec nisi erat, pulvinar id porttitor eget, vestibulum non odio. Etiam nec urna purus. Cras ut arcu et turpis ullamcorper molestie ac eu felis. Nulla feugiat, metus nec pulvinar vestibulum, sem lacus mollis velit, in ultricies est nulla ut tortor. In malesuada dolor eget justo euismod, sed porttitor lorem gravida.
                
                Etiam consequat vestibulum commodo. Nunc id sodales felis, ac porta sapien. Cras vel ipsum eu libero laoreet lobortis. Nam vel lacus non tellus porta congue. Quisque volutpat sagittis imperdiet. Aliquam faucibus egestas eros, quis volutpat ante pharetra eget. Praesent eu mauris at leo sagittis mollis in in lacus. Maecenas a dignissim massa. Cras vel iaculis augue.
                
                Donec ut bibendum risus. Nullam porta nibh et augue maximus interdum. Quisque non velit id nibh ornare finibus. Vivamus vitae magna vitae felis sodales ornare nec sed ex. Cras fringilla, dolor a cursus sollicitudin, nulla nulla porta diam, in porttitor tellus est malesuada leo. Integer suscipit a neque sed varius. Morbi at fringilla felis. Nunc accumsan dolor eget nisi vestibulum, quis hendrerit tortor sagittis. Nunc non fermentum tellus. Suspendisse porttitor mi sit amet lorem interdum, sed dapibus urna finibus. Nulla elementum finibus orci, sed tempus elit convallis at. Pellentesque vestibulum metus nec enim lobortis, nec vulputate ligula consectetur. Aenean placerat consectetur justo eu sodales. Donec vestibulum leo augue, pharetra commodo metus aliquet id.
                
                Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Aliquam aliquam quis risus quis cursus. Duis fermentum imperdiet convallis. Cras fermentum nulla augue, eu placerat libero bibendum finibus. Ut dictum gravida urna, at tristique sapien rhoncus id. Quisque placerat turpis sed sem tincidunt tincidunt. Quisque blandit lacinia sapien. Nunc ut euismod ipsum. Interdum et malesuada fames ac ante ipsum primis in faucibus. Maecenas eget quam non lorem congue molestie. Ut vel volutpat leo, a fermentum lacus. Nulla mattis vitae ipsum a accumsan. Etiam vitae tellus eu augue blandit commodo sollicitudin ac
                """;
        final var expectedLaunchedAt = Year.of(1999);
        final var expectedDuration = 78.5;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.AGE_18;
        final var expectedCategories = Set.of(CategoryID.generateUnique());
        final var expectedGenres = Set.of(GenreID.generateUnique());
        final var expectedCastMembers = Set.of(CastMemberID.generateUnique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' should be between 1 and 4000 characters";

        final var video = Video.create(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedPublished,
                expectedOpened,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );
        final var validator = new VideoValidator(video, new ThrowsValidationHandler());

        // when
        final var actualError = Assertions.assertThrows(DomainException.class, () -> validator.validate());

        // then
        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    public void givenNullLaunchedAt_whenCallingValidate_shouldReceiveError() {
        //given
        final String expectedTitle = null;
        final var expectedDescription = """
                The Blair Witch Project (1999) follows three filmmakers who venture into the Maryland woods to 
                investigate the Blair Witch legend. As strange events unfold, paranoia and fear take over, 
                leaving them unsure if they’re being hunted by a supernatural force. 
                """;
        final Year expectedLaunchedAt = null;
        final var expectedDuration = 78.5;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.AGE_18;
        final var expectedCategories = Set.of(CategoryID.generateUnique());
        final var expectedGenres = Set.of(GenreID.generateUnique());
        final var expectedCastMembers = Set.of(CastMemberID.generateUnique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'launchedAt' should not be null";

        final var video = Video.create(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedPublished,
                expectedOpened,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );
        final var validator = new VideoValidator(video, new ThrowsValidationHandler());

        // when
        final var actualError = Assertions.assertThrows(DomainException.class, () -> validator.validate());

        // then
        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenNullRating_whenCallingValidate_shouldReceiveError() {
        //given
        final String expectedTitle = "The Blair Witch Project";
        final var expectedDescription = """
                The Blair Witch Project (1999) follows three filmmakers who venture into the Maryland woods to 
                investigate the Blair Witch legend. As strange events unfold, paranoia and fear take over, 
                leaving them unsure if they’re being hunted by a supernatural force. 
                """;
        final var expectedLaunchedAt = Year.of(1999);
        final var expectedDuration = 78.5;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final Rating expectedRating = null;
        final var expectedCategories = Set.of(CategoryID.generateUnique());
        final var expectedGenres = Set.of(GenreID.generateUnique());
        final var expectedCastMembers = Set.of(CastMemberID.generateUnique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'rating' should not be null";

        final var video = Video.create(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedPublished,
                expectedOpened,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );
        final var validator = new VideoValidator(video, new ThrowsValidationHandler());

        // when
        final var actualError = Assertions.assertThrows(DomainException.class, () -> validator.validate());

        // then
        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }
}
