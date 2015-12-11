//
//  CreateAccountSocialLoginViewController.h
//  Guardioes da Saude
//
//  Created by Miqueias Lopes on 20/11/15.
//  Copyright © 2015 epitrack. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CreateAccountSocialLoginViewController : UIViewController<UITextFieldDelegate, UITextViewDelegate>
@property (weak, nonatomic) IBOutlet UITextField *txtNick;
@property (weak, nonatomic) IBOutlet UITextField *txtDob;
@property (weak, nonatomic) IBOutlet UITextField *txtEmail;
@property (weak, nonatomic) IBOutlet UISegmentedControl *segmentGender;
@property (weak, nonatomic) IBOutlet UISegmentedControl *segmentRace;
- (IBAction)btnCadastrar:(id)sender;


@end
