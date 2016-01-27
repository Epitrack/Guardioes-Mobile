//
//  ProfileFormViewController.h
//  Guardioes da Saude
//
//  Created by Miqueias Lopes on 18/11/15.
//  Copyright © 2015 epitrack. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Household.h"
#import "User.h"
#import "RMDateSelectionViewController.h"
@import DownPicker;

typedef enum operation{
    EDIT_USER,
    EDIT_HOUSEHOLD,
    ADD_HOUSEHOLD
} Operation;

@interface ProfileFormViewController : UIViewController

@property User *user;
@property Household *household;
@property Operation operation;
@property (strong, nonatomic) NSString *pictureSelected;

@property (weak, nonatomic) IBOutlet UITextField *txtNick;
@property (weak, nonatomic) IBOutlet UIButton *btnDob;
- (IBAction)btnDobAction:(id)sender;
@property (weak, nonatomic) IBOutlet UITextField *txtEmail;
@property (weak, nonatomic) IBOutlet UITextField *txtPassword;
@property (weak, nonatomic) IBOutlet UITextField *txtConfirmPassword;
- (IBAction)btnAction:(id)sender;
@property (strong, nonatomic) NSString *idUser;
@property (strong, nonatomic) NSString *idHousehold;
@property (nonatomic, assign) NSInteger *newMember; //0 = YES and 1 = NO
@property (weak, nonatomic) IBOutlet UIButton *btnPicture;
- (IBAction)btnSelectPicture:(id)sender;
@property (nonatomic, assign) NSInteger *editProfile;
@property (weak, nonatomic) IBOutlet UIDownPicker *pickerRace;
@property (weak, nonatomic) IBOutlet UIDownPicker *pickerGender;
@property (weak, nonatomic) IBOutlet UIDownPicker *pickerRelationship;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *topTxtEmailContraint;
@property (weak, nonatomic) IBOutlet UILabel *lbParentesco;

- (void) updatePicture: (NSString *) picture;

@end
