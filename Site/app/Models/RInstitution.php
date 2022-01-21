<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $instit_id
 * @property string     $instit_name
 * @property int        $instit_order
 */
class RInstitution extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'R_institution';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'instit_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'instit_id', 'instit_name', 'instit_order', 'instit_arh'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [];

    /**
     * The attributes that should be casted to native types.
     *
     * @var array
     */
    protected $casts = [
        'instit_id' => 'int', 'instit_name' => 'string', 'instit_order' => 'int'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [];

    /**
     * Indicates if the model should be timestamped.
     *
     * @var boolean
     */
    public $timestamps = false;

    // Scopes...

    // Functions ...

    // Relations ...
    public function eq_person()
    {
        return $this->hasMany(RPersons::class, 'instit_id');
    }
}
